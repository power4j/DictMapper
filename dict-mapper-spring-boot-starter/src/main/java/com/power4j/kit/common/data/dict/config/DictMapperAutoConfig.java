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

import cn.hutool.core.util.StrUtil;
import com.power4j.kit.common.data.dict.annotation.MapDict;
import com.power4j.kit.common.data.dict.support.RestResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/26
 * @since 1.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DictMapperProperties.class)
@Import(DictMapperAutoConfig.DictMapperScannerRegistrar.class)
public class DictMapperAutoConfig {

	@Bean
	@ConditionalOnMissingBean
	public RestResponseProcessor<?> restResponseProcessor() {
		return (data) -> {
			Map<String, Object> map = new HashMap<>(3);
			map.put("code", 0);
			map.put("msg", "ok");
			map.put("data", data);
			return map;
		};
	}

	public static class DictMapperScannerRegistrar
			implements EnvironmentAware, BeanFactoryAware, ImportBeanDefinitionRegistrar {

		@Nullable
		private BeanFactory beanFactory;

		@Nullable
		private Environment environment;

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {
			if (!AutoConfigurationPackages.has(this.beanFactory)) {
				log.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.");
				return;
			}

			log.debug("Searching for @Dict");

			List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
			String[] scanPackages = environment.getProperty(DictMapperProperties.PREFIX + ".scan-packages",
					String[].class, new String[0]);
			packages.addAll(Arrays.asList(scanPackages).stream().flatMap(o -> StrUtil.splitTrim(o, ",").stream())
					.collect(Collectors.toList()));
			if (log.isDebugEnabled()) {
				packages.forEach(pkg -> log.debug("Using auto-configuration base package '{}'", pkg));
			}

			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DictScannerConfigurer.class);
			builder.addPropertyValue("annotationClass", MapDict.class);
			builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
			registry.registerBeanDefinition(DictScannerConfigurer.class.getName(), builder.getBeanDefinition());
		}

		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			this.beanFactory = beanFactory;
		}

		@Override
		public void setEnvironment(Environment environment) {
			this.environment = environment;
		}

	}

}
