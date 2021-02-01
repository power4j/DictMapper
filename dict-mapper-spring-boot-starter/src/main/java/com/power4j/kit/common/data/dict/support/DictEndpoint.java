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

import cn.hutool.core.collection.CollectionUtil;
import com.power4j.kit.common.data.dict.config.DictMapperProperties;
import com.power4j.kit.common.data.dict.model.Dict;
import com.power4j.kit.common.data.dict.model.DictItem;
import com.power4j.kit.common.data.dict.repository.DictRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/1/28
 * @since 1.0
 */
@Tag(name = "常量字典")
@RestController
@RequiredArgsConstructor
@RequestMapping("${ji-boot.dict.endpoint-base-url:/sys/immutable-dictionaries}")
@ConditionalOnProperty(prefix = DictMapperProperties.PREFIX, name = "enable-endpoint", havingValue = "true",
		matchIfMissing = true)
public class DictEndpoint {

	private final DictRepository dictRepository;

	private final ObjectProvider<RestResponseProcessor<?>> restResponseProcessors;

	private final ObjectProvider<DictConverter<?>> dictConverters;

	private final ObjectProvider<DictItemConverter<?>> dictItemConverters;

	private final AtomicReference<List<Dict>> dictList = new AtomicReference<>();

	@GetMapping("/page")
	@Operation(summary = "分页")
	public ResponseEntity<?> getDictList(@RequestParam(name = "current", required = false) Integer page,
			@RequestParam(name = "limit", required = false) Integer size) {
		List<Dict> data = dictList.updateAndGet(o -> o == null ? makeDictList() : o);
		if (page == null || page < 1) {
			page = 1;
		}
		if (size == null || size < 1) {
			size = 20;
		}
		int start = (page - 1) * size;
		data = CollectionUtil.sub(data, start, start + size);
		DictConverter<?> converter = dictConverters.getIfAvailable();
		List<?> converted = data;
		if (converter != null) {
			converted = data.stream().map(converter::convert).collect(Collectors.toList());
		}
		return ResponseEntity
				.ok(restResponseProcessors.getIfAvailable(() -> RestResponseProcessor.AS_IS).process(converted));

	}

	@GetMapping("/code/{code}")
	@Operation(summary = "字典项列表")
	public ResponseEntity<?> listItems(@PathVariable("code") String code) {
		List<DictItem> data = dictRepository.get(code).map(Dict::getItems).orElse(Collections.emptyList());
		DictItemConverter<?> converter = dictItemConverters.getIfAvailable();
		List<?> converted = data;
		if (converter != null) {
			converted = data.stream().map(converter::convert).collect(Collectors.toList());
		}

		return ResponseEntity
				.ok(restResponseProcessors.getIfAvailable(() -> RestResponseProcessor.AS_IS).process(converted));
	}

	protected List<Dict> makeDictList() {
		if (dictRepository.getDictMap() != null) {
			return dictRepository.getDictMap().values().stream().sorted(Comparator.comparing(Dict::getCode))
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}
