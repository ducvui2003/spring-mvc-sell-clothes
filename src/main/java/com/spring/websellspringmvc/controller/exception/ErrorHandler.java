package com.spring.websellspringmvc.controller.exception;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.config.ConfigPage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error")
public class ErrorHandler extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        if (servletName == null) {
            servletName = "Unknown";
        }
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        String contentType = response.getContentType();
        if (contentType != null && contentType.equalsIgnoreCase("application/json;charset=UTF-8"))
            handleErrorApi(request, response);
        else
            handleErrorHTML(request, response);

    }

    private void handleErrorApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        if (servletName == null) {
            servletName = "Unknown";
        }
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        JsonObject json = new JsonObject();

//        Bắt các lỗi ném ra exception từ app
        if (throwable instanceof AppException) {
            AppException appException = (AppException) throwable;
            response.setStatus(appException.getErrorCode().getCode());
            json.addProperty("code", appException.getErrorCode().getCode());
            json.addProperty("message", appException.getErrorCode().getMessage());
        } else {
//        Bắt các lỗi khác
            response.setStatus(statusCode);
            json.addProperty("code", statusCode);
            json.addProperty("message", "Error in:" + requestUri);
        }
        response.getWriter().write(gson.toJson(json));
    }

    private void handleErrorHTML(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        if (servletName == null) {
            servletName = "Unknown";
        }
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        response.sendRedirect(ConfigPage.ERROR_404);
    }
}
