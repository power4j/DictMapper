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

package com.power4j.kit.example.enums;

import com.power4j.kit.common.data.dict.annotation.DictValue;
import com.power4j.kit.common.data.dict.annotation.Item;
import com.power4j.kit.common.data.dict.annotation.MapDict;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/27
 * @since 1.0
 */
@MapDict(items = {
		// @formatter:off
		@Item(value = "1", label = "一", style = "info"),
		@Item(value = "2", label = "二", style = "warning")
		// @formatter:on
})
public enum DemoTypeEnum {

	/**
	 * One
	 */
	One("one"),
	/**
	 * Two
	 */
	Two("two");

	private final String value;

	DemoTypeEnum(String value) {
		this.value = value;
	}

	@DictValue
	public String getValue() {
		return value;
	}

}
