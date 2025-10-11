package com.spring.websellspringmvc.config.security;

import com.spring.websellspringmvc.utils.Encoding;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Sha1Base64PasswordEncoder implements PasswordEncoder {

  private final Encoding encoding = Encoding.getINSTANCE();

  @Override
  public String encode(CharSequence rawPassword) {
    return encoding.toSHA1(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    String rawEncoded = encoding.toSHA1(rawPassword.toString());
    return rawEncoded.equals(encodedPassword);
  }
}
