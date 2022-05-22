package com.springsecurity.client.service.impl;

import com.springsecurity.client.common.RestResponse;
import com.springsecurity.client.common.RestUtils;
import com.springsecurity.client.dto.UserDto;
import com.springsecurity.client.dto.response.UserInfo;
import com.springsecurity.client.dto.response.UserRegistration;
import com.springsecurity.client.entity.User;
import com.springsecurity.client.entity.VerificationToken;
import com.springsecurity.client.event.RegistrationCompleteEvent;
import com.springsecurity.client.exception.ResourceException;
import com.springsecurity.client.repositry.UserRepository;
import com.springsecurity.client.repositry.VerificationTokenRepository;
import com.springsecurity.client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public ResponseEntity<RestResponse<UserRegistration>> registerUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null && user.isEnabled()) {
            throw new ResourceException(null, "US-409", "User Already Exist with given email", HttpStatus.CONFLICT);
        }

        if (user != null && !user.isEnabled()) {
            resendVerificationToken(user.getEmail(), httpServletRequest);
            throw new ResourceException(null, "US-410", "You have an existing account with us. A verification link has been sent to your registered email", HttpStatus.FOUND);
        }

        user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(httpServletRequest)));
        return RestUtils.successResponse(new UserRegistration("Registration is successful. We have sent an verification link on email"), HttpStatus.OK, "User Registration Success");
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public ResponseEntity<RestResponse<UserRegistration>> verifyRegistration(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new ResourceException(null, "US-411", "Invalid Token", HttpStatus.OK);
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            throw new ResourceException(null, "US-412", "token expired", HttpStatus.OK);
        }
        user.setEnabled(true);
        userRepository.save(user);
        return RestUtils.successResponse(new UserRegistration("User validated successfully"), HttpStatus.OK, "Validation Success");
    }

    @Override
    public ResponseEntity<RestResponse<UserRegistration>> resendVerificationToken(String email, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByEmail(email);
        log.info("user result: " + user);
        if (user == null) {
            throw new ResourceException(null, "US-413", "No user found with email : " + email, HttpStatus.OK);
        }

        VerificationToken verificationTokenDB = verificationTokenRepository.findByUserId(user.getId());
        log.info("verificationTokenDB " + verificationTokenDB);

        String newToken = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, newToken);
        verificationToken.setId(verificationTokenDB.getId());
        log.info("verificationToken New Object: " + verificationToken);
        verificationTokenRepository.save(verificationToken);

        sendVerificationLine(user, applicationUrl(httpServletRequest), newToken);
        return RestUtils.successResponse(new UserRegistration("Verification link has been sent successfully on your registered email"), HttpStatus.OK, "Re-send Verification Token Success");
    }

    @Override
    public UserInfo findUserLastName(String email) {
        log.info("user email :" + email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceException(null, "US-413", "Invalid User Email", HttpStatus.OK);
        }
        UserInfo userInfo = new UserInfo(user.getLastName());
        return userInfo;
    }

    private void sendVerificationLine(User user, String applicationUrl, String token) {
        String url =
                applicationUrl
                        + "/verifyRegistration?token="
                        + token;
        log.info("URL :" + url);
    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
    }
}
