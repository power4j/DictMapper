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

package com.power4j.kit.common.data.dict.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/26
 * @since 1.0
 */
@Data
public class DictItem implements Serializable {

	private final static long serialVersionUID = 1L;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 助记符
	 */
	private String label;

	/**
	 * 备注信息
	 */
	@Nullable
	private String remarks;

	/**
	 * UI渲染风格
	 */
	@Nullable
	private String style;

	/**
	 * 排序
	 */
	private int order;

	/**
	 * 自定义属性
	 */
	private Map<String, Object> extra;

}
