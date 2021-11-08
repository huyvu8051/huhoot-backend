package com.huhoot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnswerTime {
	FIVE_SEC(5), TEN_SEC(10), TWENTY_SEC(20), THIRTY_SEC(30), ONE_MINUTE(60), ONE_MINUTE_THIRTY_SEC(90),
	TWO_MINUTE(120), FOUR_MINUTE(240);

	private Integer value;
}
