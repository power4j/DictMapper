package com.power4j.kit.common.data.dict.support;

import cn.hutool.json.JSONUtil;
import com.power4j.kit.common.data.dict.model.Dict;
import com.power4j.kit.common.data.dict.model.DictItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class DefaultDictResolverTest {

	@Test
	public void testEmptyEnum() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class.forName(Tests.EmptyEnumTest.Em.class.getName()));
		DefaultDictResolver dictResolver = new DefaultDictResolver();

		Dict dict = dictResolver.resolve(enumClass).orElse(null);
		log.info(JSONUtil.toJsonPrettyStr(dict));

		Assertions.assertNotNull(dict);
		Assertions.assertEquals(dict.getCode(), Tests.EmptyEnumTest.Em.class.getSimpleName());
		Assertions.assertEquals(dict.getName(), Tests.EmptyEnumTest.Em.class.getSimpleName());
		Assertions.assertEquals(dict.getRemarks(), "");

		Assertions.assertEquals(dict.getItems().size(), 2);

		final DictItem item0 = dict.getItems().get(0);
		Assertions.assertEquals(item0.getValue(), Tests.EmptyEnumTest.VAL_0);
		Assertions.assertEquals(item0.getLabel(), Tests.EmptyEnumTest.LABEL_0);
		Assertions.assertEquals(item0.getStyle(), Tests.EmptyEnumTest.STYLE_0);

		final DictItem item1 = dict.getItems().get(1);
		Assertions.assertEquals(item1.getValue(), Tests.EmptyEnumTest.VAL_1);
		Assertions.assertEquals(item1.getLabel(), Tests.EmptyEnumTest.LABEL_1);
		Assertions.assertEquals(item1.getStyle(), Tests.EmptyEnumTest.STYLE_1);
	}

	@Test
	public void testIgnoreEnumConstant() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class.forName(Tests.IgnoreEnumConstantTest.Em.class.getName()));
		DefaultDictResolver dictResolver = new DefaultDictResolver();

		Dict dict = dictResolver.resolve(enumClass).orElse(null);
		log.info(JSONUtil.toJsonPrettyStr(dict));

		Assertions.assertNotNull(dict);
		Assertions.assertEquals(dict.getItems().size(), 1);
	}

	@Test
	public void testEnumConstant() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class.forName(Tests.EnumConstantTest.Em.class.getName()));
		DefaultDictResolver dictResolver = new DefaultDictResolver();

		Dict dict = dictResolver.resolve(enumClass).orElse(null);
		log.info(JSONUtil.toJsonPrettyStr(dict));

		Assertions.assertNotNull(dict);
		Assertions.assertEquals(dict.getCode(), Tests.EnumConstantTest.Em.class.getSimpleName());
		Assertions.assertEquals(dict.getName(), Tests.EnumConstantTest.DICT_NAME);
		Assertions.assertEquals(dict.getRemarks(), Tests.EnumConstantTest.DICT_REMARKS);

		Assertions.assertEquals(dict.getItems().size(), 3);

		final DictItem item0 = dict.getItems().get(0);
		Assertions.assertEquals(Tests.EnumConstantTest.VAL_0, item0.getValue());
		Assertions.assertEquals(Tests.EnumConstantTest.LABEL_0, item0.getLabel());
		Assertions.assertEquals(Tests.EnumConstantTest.STYLE_0, item0.getStyle());
		Assertions.assertEquals(Tests.EnumConstantTest.REMARKS_0, item0.getRemarks());

		final DictItem item1 = dict.getItems().get(1);
		Assertions.assertEquals(Tests.EnumConstantTest.VAL_1, item1.getValue());
		Assertions.assertEquals(item1.getValue(), item1.getLabel());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_STYLE, item1.getStyle());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_REMARKS, item1.getRemarks());

		final DictItem item2 = dict.getItems().get(2);
		Assertions.assertEquals(Tests.EnumConstantTest.VAL_2, item2.getValue());
		Assertions.assertEquals(item2.getValue(), item2.getLabel());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_STYLE, item2.getStyle());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_REMARKS, item2.getRemarks());
	}

}