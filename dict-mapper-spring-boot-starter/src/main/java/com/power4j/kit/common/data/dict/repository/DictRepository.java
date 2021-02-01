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

package com.power4j.kit.common.data.dict.repository;

import com.power4j.kit.common.data.dict.model.Dict;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/27
 * @since 1.0
 */
public class DictRepository {

	private Map<String, Dict> dictMap;

	/**
	 * 添加
	 * @param dict
	 */
	public void add(Dict dict) {
		dictMap.put(dict.getCode(), dict);
	}

	/**
	 * 获取
	 * @param dictCode
	 * @return
	 */
	public Optional<Dict> get(String dictCode) {
		return Optional.ofNullable(dictMap.get(dictCode));
	}

	/**
	 * 检查是否存在
	 * @param dictCode
	 * @return
	 */
	public boolean exists(String dictCode) {
		return dictMap.containsKey(dictCode);
	}

	@Nullable
	public Map<String, Dict> getDictMap() {
		return dictMap;
	}

	public void setDictMap(Map<String, Dict> dictMap) {
		this.dictMap = dictMap;
	}

}
