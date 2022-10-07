package com.example.fastlms.admin.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberParam {
    long pageIndex; // 현재 페이지 번호
    long pageSize;  // 페이지당 보여지는 데이터 개수

    private String searchType;
    private String searchValue;

    private String userId;

    /*
        pageIndex : 1 --> limit 0, 10 (pageIndex, pageSize)
        pageIndex : 2 --> limit 10, 10
        pageIndex : 3 --> limit 20, 10
        pageIndex : 4 --> limit 30, 10
     */
    public long getPageStart() {
        init();
        return (pageIndex - 1) * pageSize;
    }

    public long getPageEnd() {
        init();
        return pageSize;
    }

    public void init() {
        if (pageIndex < 1) {
            pageIndex = 1;
        }

        if (pageSize < 10) {
            pageSize = 10;
        }
    }

    public String getQueryString() {
        init();

        StringBuilder sb = new StringBuilder();

        // 검색타입이 있는 경우
        if (searchType != null && searchType.length() > 0) {
            sb.append(String.format("searchType=%s", searchType));
        }

        // 검색어가 있는 경우
        if (searchValue != null && searchValue.length() > 0) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("searchValue=%s", searchValue));
        }

        return sb.toString();
    }
}
