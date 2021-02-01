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
import com.power4j.kit.common.data.dict.annotation.Label;
import com.power4j.kit.common.data.dict.annotation.MapDict;
import com.power4j.kit.common.data.dict.annotation.Styled;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/28
 * @since 1.0
 */
@MapDict(code = "test_enum", name = "测试")
public enum FooEnum {

	/**
	 * One
	 */
	@Label("第1种方案")
	@Styled("success")
	One("one"),
	/**
	 * Two
	 */
	@Label("第2种方案")
	@Styled("warning")
	Two("two");

	@DictValue
	private final String value;

	FooEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
