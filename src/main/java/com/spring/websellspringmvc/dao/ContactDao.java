package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Contact;
import com.spring.websellspringmvc.models.SubjectContact;

import java.util.List;

public class ContactDao {

    public List<Contact> getListUserContacts(){
        return GeneralDAO.executeQueryWithSingleTable("SELECT id, fullName, phone, email, `subject` FROM contacts", Contact.class);
    }

    public List<SubjectContact> getListContactSubjects(){
        String sql = "SELECT id, subjectName FROM contact_subjects";
        return GeneralDAO.executeQueryWithSingleTable(sql, SubjectContact.class);
    }


    public int getIdContactSubjectByName(String subjectName){
        return (int) GeneralDAO.executeQueryWithJoinTables("SELECT id FROM contact_subjects WHERE subjectName = ?", subjectName).get(0).get("id");
    }

    public void addNewRecordUserContact(Integer userId, String fullName, String phone, String email, int subjectId, String message){
        GeneralDAO.executeAllTypeUpdate("INSERT INTO contacts(userId, fullName, phone, email, subjectId, message) VALUES(?,?,?,?,?,?)", userId, fullName, phone, email, subjectId, message);
    }
}
