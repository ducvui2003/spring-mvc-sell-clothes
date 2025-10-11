package com.spring.websellspringmvc.session;

import com.spring.websellspringmvc.config.security.AppUserPrincipal;
import com.spring.websellspringmvc.models.User;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionManager {

  public User getUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof AppUserPrincipal) {
      AppUserPrincipal principal = (AppUserPrincipal) auth.getPrincipal();
      return new User(principal.getId(), principal.getUsername(), principal.getEmail());
    }
    return null;
  }

  public void addUser(User user) {
    ServletRequestAttributes attrs =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attrs != null) attrs.getRequest().getSession(true).setAttribute("user", user);
  }

  public void setQuantityCart(int quantity) {
    ServletRequestAttributes attrs =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attrs != null) attrs.getRequest().getSession(true).setAttribute("quantity", quantity);
  }

  public void setQuantityCart(int quantity, HttpSession session) {
    session.setAttribute("quantity", quantity);
  }
}
