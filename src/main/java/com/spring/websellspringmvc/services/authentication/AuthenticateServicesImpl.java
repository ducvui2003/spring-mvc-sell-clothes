package com.spring.websellspringmvc.services.authentication;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorView;
import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.dto.mvc.request.SignInRequest;
import com.spring.websellspringmvc.dto.mvc.request.SignUpRequest;
import com.spring.websellspringmvc.mapper.UserMapper;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.properties.MailProperties;
import com.spring.websellspringmvc.services.mail.IMailServices;
import com.spring.websellspringmvc.services.mail.MailVerifyServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.Encoding;
import com.spring.websellspringmvc.utils.Token;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateServicesImpl implements AuthenticationService {
    UserDAO userDAO;
    UserMapper userMapper = UserMapper.INSTANCE;
    CartDAO cartDAO;
    SessionManager sessionManager;

    @Override
    public void signIn(SignInRequest dto) {
        User user = userDAO.findByUsername(dto.getUsername(), true);
        if (user == null)
            throw new AppException(new ErrorView(ErrorView.SIGN_IN_FAILED, "user", dto));

        String encode = Encoding.getINSTANCE().toSHA1(dto.getPassword());
        if (!user.getPasswordEncoding().equals(encode))
            throw new AppException(ErrorView.SIGN_IN_FAILED);
        int quantityCart = cartDAO.getQuantityCart(user.getId());
        sessionManager.addUser(user);
        sessionManager.setQuantityCart(quantityCart);
    }

    @Override
    public void signUp(SignUpRequest dto) {
        User user = userDAO.findByUsername(dto.getUsername(), true);
        if (user != null)
            throw new AppException(new ErrorView(ErrorView.SIGN_UP_FAILED, "user", dto));
        String passwordEncoding = Encoding.getINSTANCE().toSHA1(dto.getPassword());
        User userSaved = userMapper.toUser(dto);
        userSaved.setPasswordEncoding(passwordEncoding);
        createUser(userSaved);
    }

    public void createUser(User user) {
        String tokenVerify = Token.generateToken();

        Timestamp timestampExpiredToken = Token.addTime(LocalDateTime.now(), MailProperties.getDurationTokenVerify());

        user.setTokenVerify(tokenVerify);
        user.setTokenVerifyTime(timestampExpiredToken);
        user.setVerify(false);
        userDAO.insert(user);
        try {
            IMailServices mailServices = new MailVerifyServices(user.getEmail(), user.getUsername(), tokenVerify, timestampExpiredToken);
            mailServices.send();
            log.info("Send mail success");
        } catch (MessagingException ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void verify(String username, String token) {
        Optional<User> userOptional = userDAO.selectTokenVerify(username);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorView.ERROR_404));
        Timestamp userTokenExpired = user.getTokenVerifyTime();
        Timestamp timestampCurrent = Timestamp.valueOf(LocalDateTime.now());
        if (timestampCurrent.compareTo(userTokenExpired) > 0) throw new AppException(ErrorView.ERROR_404);

        userDAO.updateTokenVerify(user.getId(), true, null, null);
    }
}