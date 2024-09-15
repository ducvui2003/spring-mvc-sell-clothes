package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.models.SubjectContact;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.ContactServices;
import com.spring.websellspringmvc.session.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ContactController {
    ContactServices contactServices;
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        String fullName = request.getParameter("fullName");
//        String phone = request.getParameter("phone");
//        String email = request.getParameter("email");
//        String subject = request.getParameter("subject");
//        String message = request.getParameter("message");
//        System.out.println("message: " + message);
//        JSONObject jsonObject = new JSONObject();
//        JSONObject errorFields = new JSONObject();
//
//        boolean isContactValid = checkAllValidationContact(errorFields, fullName, email, phone);
//        jsonObject.put("isContactValid", isContactValid);
//        jsonObject.put("errorFields", errorFields);
//
//        if (isContactValid) {
//            int subjectId = ContactServices.getINSTANCE().getIdContactSubjectByName(subject);
//            User user = SessionManager.getInstance(request, response).getUser();
//            Integer userAuthId;
//
//            if (user != null) {
//                userAuthId = user.getId();
//            } else {
//                userAuthId = null;
//            }
//
//            ContactServices.getINSTANCE().addNewRecordUserContact(userAuthId, fullName, phone, email, subjectId, message);
//            String succeedContact = "<p><i class=\"fa-solid fa-circle-check\"></i> Bạn đã gửi liên hệ thành công</p>";
//            request.setAttribute("succeedContact", succeedContact);
//            jsonObject.put("succeedContact", request.getAttribute("succeedContact"));
//        }
//        response.getWriter().print(jsonObject);
//    }

    public boolean checkAllValidationContact(JSONObject errorFields, String fullName, String email, String phone) {
        boolean isAllValid = true;
        if (fullName.isEmpty()) {
            errorFields.put("fullNameError", "Vui lòng bạn nhập họ và tên");
        }

        if (email.isEmpty() || !isValidEmail(email)) {
            isAllValid = false;
            if (email.isEmpty()) {
                errorFields.put("emailError", "Vui lòng bạn nhập email");
            } else if (!isValidEmail(email)) {
                errorFields.put("emailError", "Vui lòng bạn nhập email hơp lệ (Cấu trúc email: tên_email@tên_miền)\nVí dụ: yourname@example.com");
            }
        }

        if (phone.isEmpty() || !isValidPhone(phone)) {
            isAllValid = false;
            if (phone.isEmpty()) {
                errorFields.put("phoneError", "Vui lòng bạn nhập số điện thoại");
            } else if (!isValidPhone(phone)) {
                errorFields.put("phoneError", "Vui lòng bạn nhập số điện thoại hợp lệ (Số điện thoại gồm 10 bắt đầu bằng số 0)");
            }
        }
        return isAllValid;
    }

    private boolean isValidEmail(String email) {
        Pattern patternEmail = Pattern.compile("^\\w+@\\w+\\.[A-Za-z]+$");
        Matcher matcherEmail = patternEmail.matcher(email);
        return matcherEmail.matches();
    }

    private boolean isValidPhone(String phone) {
        Pattern patternPhone = Pattern.compile("^\\+?(?:\\d\\s?){9,13}$");
        Matcher matcherPhone = patternPhone.matcher(phone);
        return matcherPhone.matches();
    }

    @GetMapping("/contact")
    public ModelAndView contact() {
        ModelAndView mav = new ModelAndView("contact");
        List<SubjectContact> listContactSubjects = contactServices.getListContactSubjects();
        mav.addObject("listContactSubjects", listContactSubjects);
        return mav;
    }
}