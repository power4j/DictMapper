package com.power4j.kit.example.config;

import cn.hutool.core.bean.BeanUtil;
import com.power4j.kit.common.data.dict.support.DictItemConverter;
import com.power4j.kit.common.data.dict.support.RestResponseProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/2/2
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class DictMapperConfig {

	@Bean
	public RestResponseProcessor<?> restResponseProcessor() {
		// 想在返回数据前处理一下，可以调整这里
		// 什么也没干，为了演示占个坑位
		return RestResponseProcessor.AS_IS;
	}

	@Bean
	public DictItemConverter<?> dictItemConverter() {
		// 想微调一下DictItem，可以调整这里
		// 根据前端需要,加了个冗余字段 color
		return (item) -> {
			Map<String, Object> data = BeanUtil.beanToMap(item);
			data.put("color", item.getStyle());
			return data;
		};
	}

}
