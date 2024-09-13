package com.spring.websellspringmvc.controller.web.account;


import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.properties.PathProperties;
import com.spring.websellspringmvc.services.image.UploadImageServices;
import com.spring.websellspringmvc.services.UserServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "UploadAvatar", value = "/UploadAvatar")
@MultipartConfig(maxFileSize = 16177215)
public class UploadAvatar extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User auth = (User) session.getAttribute("auth");
        int id = auth.getId();

        Part avatar = request.getPart("userCoverPhoto");
        String root = request.getServletContext().getRealPath("/") + PathProperties.getINSTANCE().getPathAvatarUserWeb();
        UploadImageServices uploadImageServices = new UploadImageServices(root);

        try {
            uploadImageServices.addImage(avatar);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String nameAvatar = uploadImageServices.getNameImages().get(0);

        UserServices.getINSTANCE().updateInfoUser(id, nameAvatar);
        request.getRequestDispatcher(ConfigPage.USER_ACCOUNT).forward(request, response);
    }
}