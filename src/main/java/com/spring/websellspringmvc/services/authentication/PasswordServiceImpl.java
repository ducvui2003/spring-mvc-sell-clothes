package com.spring.websellspringmvc.services.authentication;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorView;
import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.dto.mvc.request.ForgetPasswordRequest;
import com.spring.websellspringmvc.dto.mvc.request.ResetPasswordRequest;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.properties.MailProperties;
import com.spring.websellspringmvc.services.mail.IMailServices;
import com.spring.websellspringmvc.services.mail.MailResetPasswordServices;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordServiceImpl implements PasswordService {
    UserDAO userDAO;

    @Override
    public void forgetPassword(ForgetPasswordRequest dto) {
        Optional<User> userOptional = userDAO.findByEmail(dto.getEmail());
        User user = userOptional.orElseThrow(() -> new AppException(new ErrorView(ErrorView.FORGET_PASSWORD_FAILED, "email", dto)));
        String token = Token.generateToken();
        Timestamp timestampExpiredToken = Token.addTime(LocalDateTime.now(), MailProperties.getDurationTokenRestPassword());
        userDAO.updateTokenResetPassword(user.getId(), token, timestampExpiredToken);
        try {
            IMailServices mailServices = new MailResetPasswordServices(user.getEmail(), user.getEmail(), token);
            mailServices.send();
        } catch (MessagingException e) {
            log.info("Error send mail: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest dto) {
        String passwordEncoding = Encoding.getINSTANCE().toSHA1(dto.getPassword());
        Optional<User> userOptional = userDAO.findByEmail(dto.getEmail(), true);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorView.ERROR_404));
        if (user.getTokenResetPassword() == null) throw new AppException(ErrorView.ERROR_404);
        userDAO.updatePasswordEncoding(user.getId(), passwordEncoding);
        userDAO.updateTokenResetPassword(user.getId(), null, null);
    }
}
