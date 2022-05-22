package com.springsecurity.client.event.listner;

import com.springsecurity.client.entity.User;
import com.springsecurity.client.event.RegistrationCompleteEvent;
import com.springsecurity.client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user, token);
        //Send Mail to user
        String url =
                event.getAppIUrl()
                        + "/verifyRegistration?token="
                        + token;

        /**
         * Will be adding email notification
         */
        log.info("Click the link to verify your account: {}",
                url);
    }
}
