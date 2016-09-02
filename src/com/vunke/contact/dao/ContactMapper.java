package com.vunke.contact.dao;

import com.vunke.contact.model.Contact;

import java.util.List;
import java.util.Map;

public interface ContactMapper {
    int insert(Contact record);

    int update(Contact record);
    
    List<Contact> selectByIdAndName(Map<String,String> map);

    List<Contact>  findContact(Map<String,String> map );

    List<Contact> getContactAll(String userName);

    int selectTotalRows(String userName);

    List<Contact>  selectPageList  (Map<String, Object> map);

   void   deleteByIdAndUserName(Map<String, Object> map);

    int selectMaxUserId();

    List<Contact> getContactNewAll(String userName);

    int removeByContactIdAndUserName(Map<String, Object> map);
}