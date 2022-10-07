package com.example.fastlms.admin.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateInput {
    private String userId;
    private String userStatus;
    private String password;
}
