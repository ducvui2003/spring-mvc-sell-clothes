package com.spring.websellspringmvc.config.security;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity()
@Configuration()
public class WebSecurityConfig {
  @Autowired SignInSuccessHandler signInSuccessHandler;

  @Bean()
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(
            (form) ->
                form.loginPage("/signIn")
                    .loginProcessingUrl("/signIn")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(signInSuccessHandler)
                    .failureHandler(authenticationFailureHandler())
                    .permitAll())
        .logout(
            form ->
                form.logoutUrl("/signOut")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID"))
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(
            (authorize) ->
                authorize
                    .requestMatchers("/assets/**", "/js/**", "/WEB-INF/view/**")
                    .permitAll()
                    .requestMatchers("/signIn", "/", "/auth/**", "/signUp")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .csrf(
            csrf ->
                csrf.ignoringRequestMatchers(
                    new AntPathRequestMatcher("/resetPassword"),
                    new AntPathRequestMatcher("/verify")));
    return http.build();
  }

  @Bean
  public HttpFirewall allowDoubleSlashFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedDoubleSlash(true); // only this relaxed
    return firewall;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall firewall) {
    return web -> web.httpFirewall(firewall);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new Sha1Base64PasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return (request, response, exception) -> {
      String errorMessage = "Tài khoản hoặc mật khẩu sai";

      if (exception.getClass().getSimpleName().equals("LockedException")) {
        errorMessage = "Tài khoản đã bị khóa";
      }

      request.getSession().setAttribute("errors", Map.of("signInFailed", errorMessage));
      response.sendRedirect("/signIn?error=true");
    };
  }
}
