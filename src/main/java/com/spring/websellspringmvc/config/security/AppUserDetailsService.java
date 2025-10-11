package com.spring.websellspringmvc.config.security;

import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.models.User;
import jakarta.annotation.PostConstruct;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    public AppUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ AppUserDetailsService initialized!");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username, true);
        if (user == null) throw new UsernameNotFoundException("");
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
        return new AppUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPasswordEncoding(),
                authorities);
    }
}
