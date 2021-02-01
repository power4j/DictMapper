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

import cn.hutool.core.util.StrUtil;
import com.power4j.kit.common.data.dict.annotation.DictValue;
import com.power4j.kit.common.data.dict.annotation.Item;
import com.power4j.kit.common.data.dict.annotation.MapDict;
import com.power4j.kit.common.data.dict.model.Dict;
import com.power4j.kit.common.data.dict.model.DictItem;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/27
 * @since 1.0
 */
public class DefaultDictResolver implements DictResolver {

	@Override
	public Optional<Dict> resolve(Class<Enum<?>> enumClass) {
		MapDict annotation = AnnotationUtils.findAnnotation(enumClass, MapDict.class);
		if (annotation == null) {
			return Optional.empty();
		}
		Dict dict = new Dict();
		dict.setCode(lookupDictCode(annotation, enumClass.getSimpleName()));
		dict.setName(lookupDictName(annotation, dict.getCode()));
		dict.setRemarks(lookupDictRemarks(annotation, ""));
		if (StrUtil.isBlank(dict.getCode())) {
			dict.setCode(enumClass.getSimpleName());
		}
		if (StrUtil.isBlank(dict.getName())) {
			dict.setCode(dict.getCode());
		}
		dict.setItems(lookupDictItems(annotation, enumClass));
		return Optional.of(dict);
	}

	protected DictItem createDictItem(Item item) {
		return ItemResolverHelper.createDictItem(item.value(), item.label(), item.style(), item.remarks(), null);
	}

	protected String lookupDictCode(MapDict annotation, String defVal) {
		return StrUtil.isEmpty(annotation.code()) ? defVal : annotation.code();
	}

	protected String lookupDictName(MapDict annotation, String defVal) {
		return StrUtil.isEmpty(annotation.name()) ? defVal : annotation.name();
	}

	protected String lookupDictRemarks(MapDict annotation, String defVal) {
		return StrUtil.isEmpty(annotation.remarks()) ? defVal : annotation.remarks();
	}

	protected List<DictItem> lookupDictItems(MapDict annotation, Class<Enum<?>> enumClass) {
		List<DictItem> itemList = Arrays.stream(annotation.items()).map(o -> createDictItem(o))
				.collect(Collectors.toList());
		if (!itemList.isEmpty()) {
			return itemList;
		}

		ItemResolverHelper helper = new ItemResolverHelper(enumClass).lookup();

		if (!helper.getValueFunc().isPresent()) {
			throw new IllegalStateException(String.format("Could not resolve value for dict item for %s,use %s or %s",
					enumClass.getName(), DictValue.class.getSimpleName(), Item.class.getSimpleName()));
		}

		return helper.buildItemList();
	}

}
