package com.spring.websellspringmvc.session;

import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.ShoppingCartServices;
import com.spring.websellspringmvc.services.UserServices;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final String SESSION_ID = "sessionId";
    private static final String SESSION_TABLE = "sessionUser";
    private Map<String, User> sessionTable;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    UserServices userServices;

    private SessionManager(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        init();
    }

    public static SessionManager getInstance(HttpServletRequest request, HttpServletResponse response) {
        return new SessionManager(request, response);
    }

    private void init() {
        session = request.getSession();
        if (session.getAttribute(SESSION_TABLE) == null) {
            sessionTable = new HashMap<>();
        } else {
            sessionTable = (Map<String, User>) session.getAttribute(SESSION_TABLE);
        }
        session.setAttribute(SESSION_TABLE, sessionTable);
    }

    public User getUser() {
//        Cookie[] cookies = request.getCookies();
//        try {
//            if (cookies != null) {
//                for (Cookie cookie : cookies) {
//                    if (cookie.getName().equals(SESSION_ID)) {
//                        User userOld = sessionTable.get(cookie.getValue());
//                        User user = UserServices.getINSTANCE().getUser(userOld.getId());
//                        sessionTable.replace(cookie.getValue(), user);
//                        ShoppingCartServices.getINSTANCE().setUser(user);
//                        return user;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            return null;
//        }
        return null;
    }

    public void addUser(User user) {
        String sessionId = generateSessionId();
        sessionTable.put(sessionId, user);

//        ShoppingCartServices.getINSTANCE().setUser(user);
        Cookie cookie = new Cookie(SESSION_ID, sessionId);
        cookie.setPath("/");
        response.addCookie(cookie);
        session.setAttribute(SESSION_TABLE, sessionTable);
    }

    private String generateSessionId() {
        String sessionId = UUID.randomUUID().toString();
        while (sessionTable.containsKey(sessionId)) {
            sessionId = UUID.randomUUID().toString();
        }
        return sessionId;
    }

    public void removeUser() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SESSION_ID) && sessionTable.containsKey(cookie.getValue())) {
                    sessionTable.remove(cookie.getValue());
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    session.setAttribute(SESSION_TABLE, sessionTable);
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }

    public void updateUser() {
        int userId = getUser().getId();
        removeUser();
        User user = userServices.getUser(userId);
        addUser(user);
    }
}
