package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Contact;
import com.spring.websellspringmvc.models.SubjectContact;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactDAO {

    @SqlQuery("SELECT id, fullName, phone, email, `subject` FROM contacts")
    public List<Contact> getListUserContacts();

    @SqlQuery("SELECT id, subjectName FROM contact_subjects")
    public List<SubjectContact> getListContactSubjects();

    @SqlUpdate("SELECT id FROM contact_subjects WHERE subjectName = :subjectName")
    @GetGeneratedKeys
    public int getIdContactSubjectByName(@Bind("subjectName") String subjectName);

    @SqlUpdate("INSERT INTO contacts(userId, fullName, phone, email, subjectId, message) " +
            "VALUES(:userId, :fullName, :phone, :email, :subjectId, :message)")
    void addNewRecordUserContact(@Bind("userId") Integer userId,
                                 @Bind("fullName") String fullName,
                                 @Bind("phone") String phone,
                                 @Bind("email") String email,
                                 @Bind("subjectId") int subjectId,
                                 @Bind("message") String message);

}
