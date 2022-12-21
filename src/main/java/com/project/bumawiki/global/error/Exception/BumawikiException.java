package com.project.bumawiki.global.error.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BumawikiException extends RuntimeException{
    private final ErrorCode errorCode;
}
