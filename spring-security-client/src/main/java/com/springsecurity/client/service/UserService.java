package com.springsecurity.client.service;

import com.springsecurity.client.common.RestResponse;
import com.springsecurity.client.dto.UserDto;
import com.springsecurity.client.dto.response.UserInfo;
import com.springsecurity.client.dto.response.UserRegistration;
import com.springsecurity.client.entity.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseEntity<RestResponse<UserRegistration>> registerUser(UserDto userDto);

    void saveVerificationTokenForUser(User user, String token);

    ResponseEntity<RestResponse<UserRegistration>> verifyRegistration(String token);

    ResponseEntity<RestResponse<UserRegistration>> resendVerificationToken(String email, HttpServletRequest httpServletRequest);

    UserInfo findUserLastName(String email);
}
