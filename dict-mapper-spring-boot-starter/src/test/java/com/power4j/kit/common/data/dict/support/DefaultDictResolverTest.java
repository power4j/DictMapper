package com.power4j.kit.common.data.dict.support;

import cn.hutool.json.JSONUtil;
import com.power4j.kit.common.data.dict.model.Dict;
import com.power4j.kit.common.data.dict.model.DictItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@SuppressWarnings("unchecked")
class DefaultDictResolverTest {

	@Test
	public void testEmptyEnum() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class.forName(TestEnums.EmptyEnumTest.Em.class.getName()));
		DefaultDictResolver dictResolver = new DefaultDictResolver();

		Dict dict = dictResolver.resolve(enumClass).orElse(null);
		log.info(JSONUtil.toJsonPrettyStr(dict));

		Assertions.assertNotNull(dict);
		Assertions.assertEquals(dict.getCode(), TestEnums.EmptyEnumTest.Em.class.getSimpleName());
		Assertions.assertEquals(dict.getName(), TestEnums.EmptyEnumTest.Em.class.getSimpleName());
		Assertions.assertEquals(dict.getRemarks(), "");

		Assertions.assertEquals(dict.getItems().size(), 2);

		final DictItem item0 = dict.getItems().get(0);
		Assertions.assertEquals(item0.getValue(), TestEnums.EmptyEnumTest.VAL_0);
		Assertions.assertEquals(item0.getLabel(), TestEnums.EmptyEnumTest.LABEL_0);
		Assertions.assertEquals(item0.getStyle(), TestEnums.EmptyEnumTest.STYLE_0);

		final DictItem item1 = dict.getItems().get(1);
		Assertions.assertEquals(item1.getValue(), TestEnums.EmptyEnumTest.VAL_1);
		Assertions.assertEquals(item1.getLabel(), TestEnums.EmptyEnumTest.LABEL_1);
		Assertions.assertEquals(item1.getStyle(), TestEnums.EmptyEnumTest.STYLE_1);
	}

	@Test
	public void testIgnoreEnumConstant() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class
				.forName(TestEnums.IgnoreEnumConstantTest.Em.class.getName()));
		DefaultDictResolver dictResolver = new DefaultDictResolver();

		Dict dict = dictResolver.resolve(enumClass).orElse(null);
		log.info(JSONUtil.toJsonPrettyStr(dict));

		Assertions.assertNotNull(dict);
		Assertions.assertEquals(dict.getItems().size(), 1);
	}

	@Test
	public void testEnumConstant() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class.forName(TestEnums.EnumConstantTest.Em.class.getName()));
		DefaultDictResolver dictResolver = new DefaultDictResolver();

		Dict dict = dictResolver.resolve(enumClass).orElse(null);
		log.info(JSONUtil.toJsonPrettyStr(dict));

		Assertions.assertNotNull(dict);
		Assertions.assertEquals(dict.getCode(), TestEnums.EnumConstantTest.Em.class.getSimpleName());
		Assertions.assertEquals(dict.getName(), TestEnums.EnumConstantTest.DICT_NAME);
		Assertions.assertEquals(dict.getRemarks(), TestEnums.EnumConstantTest.DICT_REMARKS);

		Assertions.assertEquals(dict.getItems().size(), 3);

		final DictItem item0 = dict.getItems().get(0);
		Assertions.assertEquals(TestEnums.EnumConstantTest.VAL_0, item0.getValue());
		Assertions.assertEquals(TestEnums.EnumConstantTest.LABEL_0, item0.getLabel());
		Assertions.assertEquals(TestEnums.EnumConstantTest.STYLE_0, item0.getStyle());
		Assertions.assertEquals(TestEnums.EnumConstantTest.REMARKS_0, item0.getRemarks());

		final DictItem item1 = dict.getItems().get(1);
		Assertions.assertEquals(TestEnums.EnumConstantTest.VAL_1, item1.getValue());
		Assertions.assertEquals(item1.getValue(), item1.getLabel());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_STYLE, item1.getStyle());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_REMARKS, item1.getRemarks());

		final DictItem item2 = dict.getItems().get(2);
		Assertions.assertEquals(TestEnums.EnumConstantTest.VAL_2, item2.getValue());
		Assertions.assertEquals(item2.getValue(), item2.getLabel());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_STYLE, item2.getStyle());
		Assertions.assertEquals(DefaultDictResolver.ITEM_DEFAULT_REMARKS, item2.getRemarks());
	}

}