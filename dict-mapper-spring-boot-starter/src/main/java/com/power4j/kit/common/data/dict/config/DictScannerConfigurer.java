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

package com.power4j.kit.common.data.dict.config;

import com.power4j.kit.common.data.dict.annotation.MapDict;
import com.power4j.kit.common.data.dict.model.Dict;
import com.power4j.kit.common.data.dict.repository.DictRepository;
import com.power4j.kit.common.data.dict.support.DefaultDictResolver;
import com.power4j.kit.common.data.dict.support.DictResolver;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/27
 * @since 1.0
 */
@Slf4j
public class DictScannerConfigurer
		implements ApplicationContextAware, BeanPostProcessor, BeanDefinitionRegistryPostProcessor, InitializingBean {

	@Getter
	@Setter
	private String basePackage;

	@Getter
	@Setter
	private Class<? extends Annotation> annotationClass;

	@Nullable
	private ApplicationContext applicationContext;

	@Override
	public void afterPropertiesSet() {
		notNull(this.basePackage, "Property 'basePackage' is required");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

		DictEnumClassPathScanningCandidateComponentProvider provider = new DictEnumClassPathScanningCandidateComponentProvider();
		provider.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
		Set<BeanDefinition> beanDefinitionSet = new HashSet<>(8);
		StringUtils.commaDelimitedListToSet(basePackage)
				.forEach(pkg -> beanDefinitionSet.addAll(provider.findCandidateComponents(pkg)));

		Map<String, Dict> dictMap = new HashMap<>(beanDefinitionSet.size());
		DictResolver dictResolver = new DefaultDictResolver();
		beanDefinitionSet.forEach(bds -> {
			try {
				@SuppressWarnings("unchecked")
				Class<Enum<?>> enumClass = (Class<Enum<?>>) Class.forName(bds.getBeanClassName());
				Dict dict = dictResolver.resolve(enumClass).orElse(null);
				if (dict != null) {
					if (dictMap.containsKey(dict.getCode())) {
						log.warn("Duplicate dict code '{}' found on {}", dict.getCode(), enumClass.getName());
					}
					dictMap.put(dict.getCode(), dict);
				}
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DictRepository.class);
		builder.addPropertyValue("dictMap", dictMap);
		registry.registerBeanDefinition(DictRepository.class.getSimpleName(), builder.getBeanDefinition());
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// ignore
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Nullable
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof DictRepository) {
			ObjectProvider<DictRepositoryCustomizer> customizers = applicationContext
					.getBeanProvider(DictRepositoryCustomizer.class);
			customizers.stream().forEachOrdered(o -> o.customize((DictRepository) bean));
		}
		return bean;
	}

	private static class DictEnumClassPathScanningCandidateComponentProvider
			extends ClassPathScanningCandidateComponentProvider {

		DictEnumClassPathScanningCandidateComponentProvider() {
			super(false);
			addIncludeFilter(new IsEnumFilter());
			addIncludeFilter(new AnnotationTypeFilter(MapDict.class));
		}

		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return beanDefinition.getMetadata().isIndependent();
		}

		private static class IsEnumFilter implements TypeFilter {

			@SneakyThrows
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
					throws IOException {
				String className = metadataReader.getClassMetadata().getClassName();
				Class<?> clazz = Class.forName(className);
				return clazz.isEnum();
			}

		}

	}

}
