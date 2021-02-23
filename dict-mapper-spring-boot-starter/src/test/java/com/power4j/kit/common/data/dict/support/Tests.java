package com.power4j.kit.common.data.dict.support;

import com.power4j.kit.common.data.dict.annotation.DictValue;
import com.power4j.kit.common.data.dict.annotation.Item;
import com.power4j.kit.common.data.dict.annotation.Label;
import com.power4j.kit.common.data.dict.annotation.MapDict;
import com.power4j.kit.common.data.dict.annotation.Remarks;
import com.power4j.kit.common.data.dict.annotation.Styled;

/**
 * @author CJ (power4j@outlook.com)
 * @date 2021/2/23
 * @since 1.0
 */
public interface Tests {

	// @formatter:off
	interface EmptyEnumTest {

		String VAL_0 = "1";
		String LABEL_0 = "一";
		String STYLE_0 = "info";

		String VAL_1 = "2";
		String LABEL_1 = "二";
		String STYLE_1 = "warning";

		/**
		 * 通过 {@code MapDict},{@code Item} 注解字典信息
		 */
		@MapDict(items = {
				@Item(value = VAL_0, label = LABEL_0, style = STYLE_0),
				@Item(value = VAL_1, label = LABEL_1, style = STYLE_1)
		})
		enum Em {
			// nothing
		}
	}

	interface IgnoreEnumConstantTest {

		String VAL_0 = "1";
		String LABEL_0 = "一";
		String STYLE_0 = "info";

		/**
		 * 如果存在{@code Item} 那么不再解析枚举成员
		 */
		@MapDict(items = {
				@Item(value = VAL_0, label = LABEL_0, style = STYLE_0)
		})
		enum Em {
			ONE("1"),
			/**
			 * Option 2
			 */
			TWO("2"),
			/**
			 * Option 3
			 */
			THREE("3");

			@DictValue
			private final String val;

			Em(String val) {
				this.val = val;
			}
		}
	}

	interface EnumConstantTest {

		String DICT_NAME = "foo-options";
		String DICT_REMARKS = "this is a test enum";

		String VAL_0 = "val_0";
		String LABEL_0 = "label_0";
		String STYLE_0 = "style_0";
		String REMARKS_0 = "remarks_0";

		String VAL_1 = "val_1";

		String VAL_2 = "val_2";

		/**
		 * 从枚举成员解析字典信息
		 */
		@MapDict(name = DICT_NAME,remarks = DICT_REMARKS)
		public enum Em {
			/**
			 * Option 1
			 */
			@Label(LABEL_0)
			@Styled(STYLE_0)
			@Remarks(REMARKS_0)
			ONE(VAL_0),
			/**
			 * Option 2
			 */
			TWO(VAL_1),
			/**
			 * Option 3
			 */
			THREE(VAL_2);

			@DictValue
			private final String val;

			Em(String val) {
				this.val = val;
			}
		}
	}

	interface ZeroConfTest {
		/**
		 * 极简配置
		 */
		@MapDict
		enum Em {
			ONE,
			TWO,
			THREE;
		}
	}
	// @formatter:on

}
