package com.power4j.kit.common.data.dict.support;

import cn.hutool.json.JSONUtil;
import com.power4j.kit.common.data.dict.model.DictItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
@SuppressWarnings("unchecked")
class ItemResolverHelperTest {

	final static String ITEM_DEFAULT_STYLE = "xx";

	final static String ITEM_DEFAULT_REMARKS = "yy";

	@Test
	public void testEnumConstant() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class.forName(TestEnums.EnumConstantTest.Em.class.getName()));
		ItemResolverHelper dictResolver = new ItemResolverHelper(enumClass, ITEM_DEFAULT_STYLE, ITEM_DEFAULT_REMARKS);

		List<DictItem> itemList = dictResolver.lookup().buildItemList();
		log.info(JSONUtil.toJsonPrettyStr(itemList));

		Assertions.assertEquals(itemList.size(), 3);

		final DictItem item0 = itemList.get(0);
		Assertions.assertEquals(TestEnums.EnumConstantTest.VAL_0, item0.getValue());
		Assertions.assertEquals(TestEnums.EnumConstantTest.LABEL_0, item0.getLabel());
		Assertions.assertEquals(TestEnums.EnumConstantTest.STYLE_0, item0.getStyle());
		Assertions.assertEquals(TestEnums.EnumConstantTest.REMARKS_0, item0.getRemarks());

		final DictItem item1 = itemList.get(1);
		Assertions.assertEquals(TestEnums.EnumConstantTest.VAL_1, item1.getValue());
		Assertions.assertEquals(item1.getValue(), item1.getLabel());
		Assertions.assertEquals(ITEM_DEFAULT_STYLE, item1.getStyle());
		Assertions.assertEquals(ITEM_DEFAULT_REMARKS, item1.getRemarks());

		final DictItem item2 = itemList.get(2);
		Assertions.assertEquals(TestEnums.EnumConstantTest.VAL_2, item2.getValue());
		Assertions.assertEquals(item2.getValue(), item2.getLabel());
		Assertions.assertEquals(ITEM_DEFAULT_STYLE, item2.getStyle());
		Assertions.assertEquals(ITEM_DEFAULT_REMARKS, item2.getRemarks());
	}

	@Test
	public void testZeroConf() throws ClassNotFoundException {
		Class<Enum<?>> enumClass = (Class<Enum<?>>) (Class.forName(TestEnums.ZeroConfTest.Em.class.getName()));
		ItemResolverHelper dictResolver = new ItemResolverHelper(enumClass, ITEM_DEFAULT_STYLE, ITEM_DEFAULT_REMARKS);

		List<DictItem> itemList = dictResolver.lookup().buildItemList();
		log.info(JSONUtil.toJsonPrettyStr(itemList));

		Assertions.assertEquals(itemList.size(), 3);

		final DictItem item0 = itemList.get(0);
		Assertions.assertEquals("0", item0.getValue());

		final DictItem item1 = itemList.get(1);
		Assertions.assertEquals("1", item1.getValue());

		final DictItem item2 = itemList.get(2);
		Assertions.assertEquals("2", item2.getValue());
	}

}