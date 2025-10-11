package com.spring.websellspringmvc.config.security;

import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInSuccessHandler implements AuthenticationSuccessHandler {

  private final CartDAO cartDAO;
  private final SessionManager sessionManager;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    Object principal = authentication.getPrincipal();

    if (principal instanceof AppUserPrincipal) {
      AppUserPrincipal userPrincipal = (AppUserPrincipal) principal;
      int quantityCart = cartDAO.getQuantityCart(userPrincipal.getId());
      sessionManager.setQuantityCart(quantityCart, request.getSession(true));
    }
    response.sendRedirect(PageAddress.HOME.getPage());
  }
}
