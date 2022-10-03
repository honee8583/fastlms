package com.example.fastlms.member.exception;

public class MemberEmailNotAuthenticatedException extends RuntimeException {
    public MemberEmailNotAuthenticatedException(String error) {
        super(error);
    }
}
