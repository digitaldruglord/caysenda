package com.nomi.caysenda.api.web.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeliveryRegister {
    String userName;
    String email;
    String phoneNumber;
    String password;
    String confirmPassword;

}
