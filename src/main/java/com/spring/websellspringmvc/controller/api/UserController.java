package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.OrderResponseDTO;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.properties.PathProperties;
import com.spring.websellspringmvc.services.HistoryService;
import com.spring.websellspringmvc.services.UserServices;
import com.spring.websellspringmvc.services.image.UploadImageServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.Encoding;
import com.spring.websellspringmvc.utils.ValidatePassword;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserServices userServices;
    HistoryService historyService;
    SessionManager sessionManager;

    @PostMapping("/upload-avatar")
    public void uploadAvatar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = sessionManager.getUser();
        Part avatar = request.getPart("avatar");
        String root = PathProperties.getINSTANCE().getPathAvatarUserWeb();
        UploadImageServices uploadImageServices = new UploadImageServices(root);
        try {
            uploadImageServices.addImage(avatar);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String nameAvatar = uploadImageServices.getNameImages().get(0);
        JSONObject json = new JSONObject();
        userServices.updateInfoUser(user.getId(), nameAvatar);
        user.setAvatar(nameAvatar);
        json.put("status", "success");
        json.put("message", "Upload avatar success");
        response.setStatus(200);
        response.getWriter().print(json);
    }

    @PostMapping("/password")
    public void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject json = new JSONObject();

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        User user = sessionManager.getUser();

        if (currentPassword == null || newPassword == null || confirmPassword == null) {
            json.put("error", "Missing required fields");
            json.put("isValid", false);
            response.getWriter().println(json.toString());
            return;
        }

        ValidatePassword validatePassword = new ValidatePassword(newPassword);
        boolean isValid = validatePassword.check();
        if (isValid) {
            userServices.updateUserPassword(user.getId(), Encoding.getINSTANCE().toSHA1(newPassword));
            json.put("isValid", true);
        } else {
            json.put("isValid", false);
            json.put("error", validatePassword.getErrorMap());
        }
        response.getWriter().println(json.toString());
    }

    @PostMapping("/info")
    protected void changeInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = sessionManager.getUser();
        int userId = user.getId();
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthDayString = request.getParameter("birthDay");
        Date birthDay = null;
        try {
            birthDay = formatDate(birthDayString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        userServices.updateUserByID(userId, fullName, gender, phone, birthDay);
        try {
            // Nghỉ đảm bảo trong db cập nhật trước
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        SessionManager.getInstance(request, response).updateUser();
        JSONObject json = new JSONObject();
        json.put("status", "success");
        response.setStatus(200);
        response.getWriter().print(json);
    }

    private Date formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parse the String to obtain a LocalDate object
        LocalDate localDate = LocalDate.parse(date, formatter);

        // Convert the LocalDate to a java.sql.Date
        Date sqlDate = Date.valueOf(localDate);
        return sqlDate;
    }

    @GetMapping("/order")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int statusId = Integer.parseInt(request.getParameter("statusId"));
        User user = sessionManager.getUser();
        List<OrderResponseDTO> orders = historyService.getOrder(user.getId(), statusId);
        JSONObject json = new JSONObject();
        json.put("data", orders);
        response.getWriter().print(json);
    }
}
