package com.spring.websellspringmvc.session;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.controller.exception.UnAuthorizedException;
import com.spring.websellspringmvc.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionManager {
    HttpSession session;
    HttpServletRequest request;

    public User getUser() {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            throw new UnAuthorizedException();
//        }
        return user;
    }

    public void addUser(User user) {
        session.setAttribute("user", user);
    }

    public void removeUser() {
        session.removeAttribute("user");
    }

    public void clearSession() {
        session.invalidate();
    }

    public void setQuantityCart(int quantity) {
        session.setAttribute("quantity", quantity);
    }

    public void increaseQuantityCart() {
        int quantity = getQuantityCart();
        session.setAttribute("quantity", quantity + 1);
    }

    public void decreaseQuantityCart() {
        int quantity = getQuantityCart();
        session.setAttribute("quantity", quantity - 1);
    }

    public int getQuantityCart() {
        return session.getAttribute("quantity") == null ? 0 : (int) session.getAttribute("quantity");
    }
}
