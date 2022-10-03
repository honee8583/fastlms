package com.example.fastlms.member.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResetPasswordInput {
    private String userId;
    private String userName;

    private String id;
    private String password;
}
