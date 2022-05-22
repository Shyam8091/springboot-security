package com.springsecurity.client.resource;

import com.springsecurity.client.common.RestResponse;
import com.springsecurity.client.dto.UserDto;
import com.springsecurity.client.dto.response.UserRegistration;
import com.springsecurity.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;




    @PostMapping("/register")
    public ResponseEntity<RestResponse<UserRegistration>> registerUser(@RequestBody UserDto userDto, final HttpServletRequest httpServletRequest) {
        return userService.registerUser(userDto);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<RestResponse<UserRegistration>> verifyRegistration(@RequestParam("token") String token) {
        return userService.verifyRegistration(token);

    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
    }

    @PostMapping("/resendVerificationToken")
    public ResponseEntity<RestResponse<UserRegistration>> resendVerificationToken(@RequestParam String email, HttpServletRequest httpServletRequest) {
        return userService.resendVerificationToken(email, httpServletRequest);
    }


}
