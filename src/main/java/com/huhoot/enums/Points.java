package com.huhoot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Points {
	STANDARD(1), DOUBLE_POINTS(2), NO_POINTS(0);
	private Integer value;
}
