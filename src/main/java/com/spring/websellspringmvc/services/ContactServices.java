package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.ContactDAO;
import com.spring.websellspringmvc.models.Contact;
import com.spring.websellspringmvc.models.SubjectContact;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactServices {

    ContactDAO contactDao;
     
    public List<Contact> getListUserContacts() {
        return contactDao.getListUserContacts();
    }

    public List<SubjectContact> getListContactSubjects() {
        return contactDao.getListContactSubjects();
    }

    public int getIdContactSubjectByName(String subjectName) {
        return contactDao.getIdContactSubjectByName(subjectName);
    }

    public void addNewRecordUserContact(Integer userId, String fullName, String phone, String email, int subjectId, String message) {
        contactDao.addNewRecordUserContact(userId, fullName, phone, email, subjectId, message);
    }
}
