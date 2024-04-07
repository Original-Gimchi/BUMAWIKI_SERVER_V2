package com.project.bumawiki.domain.coin.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class MoneyNotEnoughException extends BumawikiException {
	public MoneyNotEnoughException() {
		super(ErrorCode.MONEY_NOT_ENOUGH);
	}
}
