package com.project.bumawiki.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	//Like
	ALREADY_THUMBS_UP(400, "THUMBSUP-400-1", "이미 이 문서에 좋아요를 눌렀습니다."),
	YOU_DONT_THUMBS_UP_THIS_DOCS(400, "THUMBSUP-400-2", "이 문서에 좋아요를 누르지 않았습니다."),
	NO_DOCS_YOU_THUMBS_UP(404, "THUMBSUP-404-1", "좋아요를 누르려는 문서가 없습니다."),

	//Docs
	DOCS_IS_NOT_CONFLICTED(400, "DOCS-400-1", "문서가 충돌나지 않았습니다."),
	DOCS_CONFLICTED(400, "DOCS-400-2", "문서가 충돌 상태이기 때문에 수정할 수 없습니다."),
	NO_UPDATABLE_DOCS(403, "DOCS-403-1", "없데이트 하려고 하는 문서가 없습니다."),
	DOCS_TITLE_ALREADY_EXIST(403, "DOCS-403-1", "같은 제목의 문서가 이미 존재합니다."),
	CANNOT_CHANGE_YOUR_DOCS(403, "DOCS-403-2", "본인의 문서는 수정할 수 없습니다."),
	DOCS_NOT_FOUND(404, "DOCS-404-2", "문서를 조회할 수 없습니다."),
	DOCS_TYPE_NOT_FOUND(404, "DOCS-404-3", "문서 타입이 존재하지 않습니다."),
	VERSION_NOT_EXIST(404, "DOCS-404-4", "문서 버전이 존재하지 않습니다."),
	NO_SUCH_VERSION(404, "DOCS-404-5", "문서 버전이 존재하지 않습니다."),

	FORBIDDEN(403, "COMMON-403-1", "Forbidden"),

	//User
	USER_NOT_LOGIN(403, "USER-403-1", "로그인하지 않았습니다."),
	USER_NOT_ADMIN(403, "USER-403-2", "관리자 권한이 없습니다."),
	USER_NOT_FOUND(404, "USER-404-1", "사용자를 조회할 수 없습니다."),

	//JWT
	INVALID_JWT(403, "TOKEN-403-1", "잘못된 토큰으로 접근하였습니다."),
	EXPIRED_JWT(403, "TOKEN-403-2", "액세스 토큰이 만료되었습니다."),
	REFRESH_TOKEN_EXPIRED(403, "TOKEN-403-3", "리프레시 토큰이 만료되었습니다."),
	TOKEN_MISSING(403, "TOKEN-403-4", "토큰이 없습니다."),

	//ServerError,
	INVALID_ARGUMENT(400, "ARG-400-1", "Arg Is Not Valid"),
	BSM_AUTH_INVALID_CLIENT(500, "BSM-500-1", "Bsm Client Is Invalid"),
	INTERNAL_SERVER_ERROR(500, "SERVER-500-1", "Internal Server Error"),

	//Image,
	NO_IMAGE(400, "IMG-400-1", "이미지가 없습니다."),
	IMAGE_NOT_FOUND_EXCEPTION(404, "IMG-404-1", "이미지를 조회할 수 없습니다."),
	S3_SAVE_EXCEPTION(500, "IMG-500-1", "S3 Save Exception"),

	//coin,
	MONEY_NOT_ENOUGH(400, "COIN-400-1", "돈이 충분하지 않습니다."),
	COIN_NOT_ENOUGH(400, "COIN-400-2", "코인이 충분하지 않습니다."),
	TRADE_ALREADY_FINISHED(400, "COIN-400-3", "거래가 이미 종료되었습니다."),
	ALREADY_AWARDED(400, "COIN-400-4", "이미 보상을 받았습니다."),
	ALREADY_CREATED(401, "COIN-401-1", "코인 지갑이 이미 생성되었습니다."),
	CANCEL_OTHERS_TRADE(403, "COIN-403-1", "다른 사람의 거래를 취소할 수 없습니다."),
	COIN_ACCOUNT_NOT_FOUND_EXCEPTION(404, "COIN-404-1", "코인 계정을 찾을 수 없습니다."),
	NO_PERIOD(404, "COIN-404-2", "기간이 없습니다."),
	PRICE_NOT_FOUND(404, "COIN-404-2", "가격을 찾을 수 없습니다."),
	TRADE_NOT_FOUND(404, "COIN-404-3", "거래를 조회할 수 없습니다."),
	RANDOM_INSTANCE(500, "COIN-500-1", "Random Instance Create Exception");

	private final int status;
	private final String code;
	private final String message;
}
