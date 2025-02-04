package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.ChangePasswordRequest;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.properties.PathProperties;
import com.spring.websellspringmvc.services.order.OrderServicesImpl;
import com.spring.websellspringmvc.services.image.UploadImageServices;
import com.spring.websellspringmvc.services.user.UserServicesImpl;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.Encoding;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserServicesImpl userServices;
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

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody @Valid ChangePasswordRequest request) throws IOException {
        User user = sessionManager.getUser();
        userServices.changePassword(user.getId(), Encoding.getINSTANCE().toSHA1(request.getNewPassword()));

        return ResponseEntity.ok(new ApiResponse<>(HttpServletResponse.SC_OK, "Change password success", null));
    }

    @PostMapping("/info")
    protected void changeInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
}
