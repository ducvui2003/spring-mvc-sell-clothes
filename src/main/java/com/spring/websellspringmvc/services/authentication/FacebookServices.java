package com.spring.websellspringmvc.services.authentication;

import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.services.http.facebook.FacebookAccessTokenResponse;
import com.spring.websellspringmvc.services.http.facebook.FacebookClientAccessToken;
import com.spring.websellspringmvc.services.http.facebook.FacebookClientUserInfo;
import com.spring.websellspringmvc.services.http.facebook.FacebookUser;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service("facebookService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FacebookServices implements OAuthServices {
    UserDAO userDAO;
    SessionManager sessionManager;
    FacebookClientUserInfo facebookClientUserInfo;
    FacebookClientAccessToken facebookClientAccessToken;
    @Value("${app.service.facebook.client-id}")
    @NonFinal
    String clientId;
    @Value("${app.service.facebook.redirect-uri}")
    @NonFinal
    String redirectUri;
    @Value("${app.service.facebook.client-secret}")
    @NonFinal
    String clientSecret;

    @Override
    public String getToken(String code) throws IOException {
        FacebookAccessTokenResponse accessToken = facebookClientAccessToken.getAccessToken(clientId, clientSecret, redirectUri, code);
        return accessToken.getAccessToken();
    }

    @Override
    public Object getUserInfo(String accessToken) throws IOException {
        return facebookClientUserInfo.getUserInfo(accessToken);
    }

    @Override
    public void signIn(String code) throws IOException {
        String accessToken = getToken(code);
        // Retrieve user's email address
        FacebookUser userFacebook = (FacebookUser) getUserInfo(accessToken);
        String email = userFacebook.getEmail();
        Optional<com.spring.websellspringmvc.models.User> userOptional = userDAO.findById(email);
        if (userOptional.isPresent())
            // Lưu thông tin user vào session
            sessionManager.addUser(userOptional.get());
        else {
            com.spring.websellspringmvc.models.User user = com.spring.websellspringmvc.models.User.builder()
                    .role(Role.USER.name())
                    .email(email)
                    .fullName(userFacebook.getName())
                    .build();
            userDAO.insert(user);
            sessionManager.addUser(user);
        }
    }
}