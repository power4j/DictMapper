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

/**
 * 在数据返回前端之前进行处理
 *
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/28
 * @since 1.0
 */
@FunctionalInterface
public interface RestResponseProcessor<R> {

	RestResponseProcessor<?> AS_IS = (o -> o);

	/**
	 * 对响应内容进行处理
	 * @param payload
	 * @return 返回处理后的对象
	 */
	R process(Object payload);

}
