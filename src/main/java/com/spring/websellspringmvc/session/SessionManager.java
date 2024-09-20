package com.spring.websellspringmvc.session;

import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.UserServices;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionManager {
    HttpSession session;

    public User getUser() {
        return session.getAttribute("user") == null ? null : (User) session.getAttribute("user");
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
}
