package com.vunke.contact.service;

import com.vunke.contact.model.Contact;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface ContactCoreService {
    Map<String, Long> saveOrUpdate(List<Contact> list, String contactName) throws Exception;

    List<Contact> getContactAll(String userName)throws Exception;
     List<Contact> findPageList(String userName,long page,int size) throws Exception;

    void deleteByIdAndUserName(Map<String,Object> map) throws Exception;

    List<Contact> queryContactAll(String userName) throws Exception;


    int removeByContactIdAndUserName(Map<String,Object> map) throws Exception;//二期

    Contact findContact(String userName,String contactPhone) throws Exception;
}
