package com.spring.websellspringmvc.services.authentication;

import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.http.google.GoogleClientAccessToken;
import com.spring.websellspringmvc.services.http.google.GoogleClientUserInfo;
import com.spring.websellspringmvc.services.http.google.GoogleUser;
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

@Service("googleService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleServices implements OAuthServices {
    SessionManager sessionManager;
    UserDAO userDAO;
    GoogleClientAccessToken googleClientAccessToken;
    GoogleClientUserInfo googleClientUserInfo;
    @Value("${app.service.google.client-id}")
    @NonFinal
    String clientId;
    @Value("${app.service.google.redirect-uri}")
    @NonFinal
    String redirectUri;
    @Value("${app.service.google.client-secret}")
    @NonFinal
    String clientSecret;
    @Value("${app.service.google.grant-type}")
    @NonFinal
    String grantType;
    CartDAO cartDAO;

    @Override
    public String getToken(String code) throws IOException {
        return googleClientAccessToken.getAccessToken(code, clientId, clientSecret, redirectUri, grantType).getAccessToken();
    }

    @Override
    public Object getUserInfo(String accessToken) throws IOException {
        return googleClientUserInfo.getUserInfo(accessToken);
    }

    @Override
    public void signIn(String code) throws IOException {
        String accessToken = getToken(code);
        GoogleUser googleUserAccount = (GoogleUser) getUserInfo(accessToken);
        String emailGoogle = googleUserAccount.getEmail();
        Optional<User> userOptional = userDAO.findById(emailGoogle);
        if (userOptional.isPresent()) {
            sessionManager.addUser(userOptional.get());
            int quantityCart = cartDAO.getQuantityCart(userOptional.get().getId());
            sessionManager.setQuantityCart(quantityCart);
        }
        else {
            User user = User.builder()
                    .role(Role.USER.name())
                    .email(googleUserAccount.getEmail())
                    .build();
            userDAO.insert(user);
            sessionManager.addUser(user);
        }
    }
}