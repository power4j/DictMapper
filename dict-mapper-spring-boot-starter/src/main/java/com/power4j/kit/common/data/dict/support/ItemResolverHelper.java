/*
 * Copyright 2020 ChenJun (power4j@outlook.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.power4j.kit.common.data.dict.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.power4j.kit.common.data.dict.annotation.DictValue;
import com.power4j.kit.common.data.dict.annotation.Label;
import com.power4j.kit.common.data.dict.annotation.Remarks;
import com.power4j.kit.common.data.dict.annotation.Styled;
import com.power4j.kit.common.data.dict.model.DictItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/28
 * @since 1.0
 */
@Slf4j
public class ItemResolverHelper {

	private final MultiValueMap<Class<? extends Annotation>, Field> fieldMap = new LinkedMultiValueMap<>(8);

	private final Map<Class<? extends Annotation>, Method> methodMap = new HashMap<>(8);

	private final Class<Enum<?>> enumClass;

	private final Map<Object, Object> labelMap = new HashMap<>(8);

	private final Map<Object, Object> styleMap = new HashMap<>(8);

	private final Map<Object, Object> remarksMap = new HashMap<>(8);

	@Nullable
	private Function<Enum<?>, Object> labelFunc;

	@Nullable
	private Function<Enum<?>, Object> valueFunc;

	@Nullable
	private Function<Enum<?>, Object> styleFunc;

	@Nullable
	private Function<Enum<?>, Object> remarksFunc;

	@Nullable
	private String defaultStyle;

	@Nullable
	private String defaultRemarks;

	public static DictItem createDictItem(String value, @Nullable String label, @Nullable String style,
			@Nullable String remarks, @Nullable Map<String, Object> extra) {
		DictItem dictItem = new DictItem();
		dictItem.setValue(value);
		dictItem.setLabel(label);
		dictItem.setStyle(style);
		dictItem.setRemarks(remarks);
		dictItem.setExtra(extra == null ? Collections.emptyMap() : extra);
		return dictItem;
	}

	public ItemResolverHelper(Class<Enum<?>> enumClass) {
		this.enumClass = enumClass;
	}

	public ItemResolverHelper(Class<Enum<?>> enumClass, String defaultStyle, String defaultRemarks) {
		this.enumClass = enumClass;
		this.defaultStyle = defaultStyle;
		this.defaultRemarks = defaultRemarks;
	}

	public Optional<Function<Enum<?>, Object>> getLabelFunc() {
		return Optional.ofNullable(labelFunc);
	}

	public Optional<Function<Enum<?>, Object>> getValueFunc() {
		return Optional.ofNullable(valueFunc);
	}

	public Optional<Function<Enum<?>, Object>> getStyleFunc() {
		return Optional.ofNullable(styleFunc);
	}

	public Optional<Function<Enum<?>, Object>> getRemarksFunc() {
		return Optional.ofNullable(remarksFunc);
	}

	public boolean fullFilled() {
		return (labelFunc != null && valueFunc != null && styleFunc != null && remarksFunc != null);
	}

	public ItemResolverHelper lookup() {
		scanAnnotationMethods(enumClass);
		scanAnnotationFields(enumClass);

		if (!processFields()) {
			processMethods();
		}
		return this;
	}

	public List<DictItem> buildItemList() {
		List<DictItem> itemList = new ArrayList<>(4);
		for (Object object : enumClass.getEnumConstants()) {
			final Enum<?> em = (Enum<?>) object;
			String value = getValueFunc().map(f -> f.apply(em)).map(Object::toString)
					.orElseThrow(() -> new IllegalStateException("No value for " + enumClass.getName()));
			String label = getLabelFunc().map(f -> f.apply(em)).map(Object::toString).orElse(value);
			String style = getStyleFunc().map(f -> f.apply(em)).map(Object::toString).orElse(defaultStyle);
			String remarks = getRemarksFunc().map(f -> f.apply(em)).map(Object::toString).orElse(defaultRemarks);
			itemList.add(createDictItem(value, label, style, remarks, null));
		}
		return itemList;
	}

	protected boolean processFields() {
		if (valueFunc == null) {
			valueFunc = fieldMap.containsKey(DictValue.class) ? (o -> filedGet(o, fieldMap.getFirst(DictValue.class)))
					: (Enum::ordinal);
		}
		return fullFilled();
	}

	protected boolean processMethods() {
		if (valueFunc == null && methodMap.containsKey(DictValue.class)) {
			valueFunc = (o -> methodGet(o, methodMap.get(DictValue.class)));
		}
		if (labelFunc == null && methodMap.containsKey(Label.class)) {
			labelFunc = (o -> methodGet(o, methodMap.get(Label.class)));
		}
		if (styleFunc == null && methodMap.containsKey(Styled.class)) {
			styleFunc = (o -> methodGet(o, methodMap.get(Styled.class)));
		}
		if (remarksFunc == null && methodMap.containsKey(Remarks.class)) {
			remarksFunc = (o -> methodGet(o, methodMap.get(Remarks.class)));
		}
		return fullFilled();
	}

	protected final String makeEnumConstantKey(Class<Enum<?>> enumClass, Field field) {
		return enumClass.getName() + "." + field.getName();
	}

	protected final String makeEnumConstantKey(Enum<?> em) {
		return em.getClass().getName() + "." + em.name();
	}

	protected void scanAnnotationFields(Class<Enum<?>> enumClass) {
		final Field[] fields = ClassUtil.getDeclaredFields(enumClass);
		for (Field field : fields) {
			boolean enumConstant = field.isEnumConstant();
			if (field.isAnnotationPresent(DictValue.class)) {
				if (enumConstant) {
					log.warn("@{} on enum constant is not allowed", DictValue.class.getSimpleName());
				}
				else {
					fieldMap.add(DictValue.class, field);
				}
			}
			if (field.isAnnotationPresent(Label.class)) {
				if (!enumConstant) {
					log.warn("@{} should annotation on enum constant field", Label.class.getSimpleName());
				}
				else {
					Label label = field.getAnnotation(Label.class);
					if (StrUtil.isNotEmpty(label.value())) {
						labelMap.put(makeEnumConstantKey(enumClass, field), label.value());
					}
					labelFunc = (o -> labelMap.get(makeEnumConstantKey(o)));
				}
			}
			if (field.isAnnotationPresent(Styled.class)) {
				if (!enumConstant) {
					log.warn("@{} should annotation on enum constant field", Styled.class.getSimpleName());
				}
				else {
					Styled styled = field.getAnnotation(Styled.class);
					if (StrUtil.isNotEmpty(styled.value())) {
						styleMap.put(makeEnumConstantKey(enumClass, field), styled.value());
					}
					styleFunc = (o -> styleMap.get(makeEnumConstantKey(o)));
				}
			}
			if (field.isAnnotationPresent(Remarks.class)) {
				if (!enumConstant) {
					log.warn("@{} should annotation on enum constant field", Remarks.class.getSimpleName());
				}
				else {
					Remarks remarks = field.getAnnotation(Remarks.class);
					if (StrUtil.isNotEmpty(remarks.value())) {
						remarksMap.put(makeEnumConstantKey(enumClass, field), remarks.value());
					}
					remarksFunc = (o -> remarksMap.get(makeEnumConstantKey(o)));
				}
			}
		}
	}

	protected void scanAnnotationMethods(Class<Enum<?>> enumClass) {
		final Method[] methods = ReflectUtil.getPublicMethods(enumClass);
		for (Method method : methods) {
			if (method.isAnnotationPresent(DictValue.class)) {
				methodMap.put(DictValue.class, method);
			}
			if (method.isAnnotationPresent(Label.class)) {
				methodMap.put(Label.class, method);
			}
			if (method.isAnnotationPresent(Styled.class)) {
				methodMap.put(Styled.class, method);
			}
		}
	}

	protected Object filedGet(Object instance, Field field) {
		try {
			field.setAccessible(true);
			return field.get(instance);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected Object methodGet(Object instance, Method method) {
		try {
			return method.invoke(instance);
		}
		catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
