package com.example.fastlms.member.entity;

public interface MemberCode {
    /**
     * 현재 가입 요청 중
     */
    String MEMBER_STATUS_REQ = "REQ";


    /**
     * 현재 이용중인 상태
     */
    String MEMBER_STATUS_AVAILABLE = "AVAILABLE";

    /**
     * 정지 상태
     */
    String MEMBER_STATUS_STOPPED = "STOPPED";
}
