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

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/26
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties(DictMapperProperties.PREFIX)
public class DictMapperProperties {

	public static final String PREFIX = "dict-mapper";

	/**
	 * 扫描的包路径,支持逗号分隔符
	 */
	private String scanPackages;

	/**
	 * 是否启用默认 REST 端点
	 */
	private Boolean enableEndpoint;

	/**
	 * 默认 REST 端点的URL路径
	 */
	private String endpointBaseUrl;

}
