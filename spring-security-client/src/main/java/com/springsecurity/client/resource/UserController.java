package com.springsecurity.client.resource;

import com.springsecurity.client.common.RestResponse;
import com.springsecurity.client.common.RestUtils;
import com.springsecurity.client.dto.response.UserInfo;
import com.springsecurity.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUserLastName")
    public ResponseEntity<RestResponse<UserInfo>> userLastname(@RequestParam String email) {
        UserInfo userLastName = userService.findUserLastName(email);
        return RestUtils.successResponse(userLastName, HttpStatus.OK, "Success");
    }
}

