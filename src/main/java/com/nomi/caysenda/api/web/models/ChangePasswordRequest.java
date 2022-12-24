package com.nomi.caysenda.api.web.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    String token;
    String oldPassword;
    String newPassword;
    String confirmNewPassword;
}
