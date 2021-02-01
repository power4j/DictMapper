package com.yo.ho;

import com.power4j.kit.common.data.dict.annotation.Item;
import com.power4j.kit.common.data.dict.annotation.MapDict;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/2/2
 * @since 1.0
 */
@MapDict(items = {
		// @formatter:off
		@Item(value = "1", label = "一", style = "info"),
		@Item(value = "2", label = "二", style = "warning")
		// @formatter:on
})
public enum OutSideEnum {

	/**
	 * ONE
	 */
	ONE,
	/**
	 * TWO
	 */
	TWO

}
