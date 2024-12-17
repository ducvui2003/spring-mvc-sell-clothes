package com.spring.websellspringmvc.session;

import com.spring.websellspringmvc.models.User;
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
