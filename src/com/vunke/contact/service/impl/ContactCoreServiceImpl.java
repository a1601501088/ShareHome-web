package com.vunke.contact.service.impl;

import com.vunke.contact.dao.ContactMapper;
import com.vunke.contact.model.Contact;
import com.vunke.contact.service.ContactCoreService;
import com.vunke.shareHome.util.Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("conCoreServ")
public class ContactCoreServiceImpl implements ContactCoreService {

    @Resource
    private ContactMapper contactMapper;

    /**
     * 将传入的数据插入数据库中
     */
    @Override
    public Map<String, Long> saveOrUpdate(List<Contact> list, String userName) throws Exception {
        Map<String, Long> map = new HashMap<String, Long>();
        long upcount = 0;
        long incount = 0;


        Map<String, String> me = new HashMap<String, String>();
        for (Contact c : list) {
            if (c.getContactName() == null || "我的手机".equals(c.getContactName()) || "手机看家".equals(c.getContactName()))
                continue;
            me.put("userId", c.getUserId());
            me.put("userName", userName);
            List<Contact> ms = contactMapper.selectByIdAndName(me);
            Contact m = null;
            if (ms != null && ms.size() > 0) {
                m = ms.get(0);
            }
            if (m != null) {//数据库中存在
                c.setUserName(userName);
                int i = contactMapper.update(c);
                upcount++;
            } else {
                /**
                 * 设置联系人的账号
                 */
                c.setUserName(userName);
                contactMapper.insert(c);
                incount++;
            }
        }
        map.put("upcount", upcount);
        map.put("incount", incount);
        return map;
    }

    /**
     * 获取所有联系人
     */
    @Override
    public List<Contact> getContactAll(String userName) throws Exception {
        return contactMapper.getContactAll(userName);
    }

    /**
     * 获取所有联系人
     */
    @Override
    public List<Contact> queryContactAll(String userName) throws Exception {
        return contactMapper.getContactNewAll(userName);
    }


    @Override
    public List<Contact> findPageList(String userName, long page, int size) throws Exception {
        if (page <= 0) {
            page = 1;
        }

        long startRow = (page - 1) * size;
        long endRow = size + 1;

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("endRow", endRow);
        map.put("startRow", startRow);
        map.put("userName", userName);
        List<Contact> contacts = contactMapper.selectPageList(map);

        return contacts;
    }

    @Override
    public void deleteByIdAndUserName(Map<String, Object> map) throws Exception {
        contactMapper.deleteByIdAndUserName(map);
    }

    /**
     * 批量删除联系人 二期
     *
     * @param map 联系人id集合，与用户名
     * @return
     * @throws Exception
     */
    @Override
    public int removeByContactIdAndUserName(Map<String, Object> map) throws Exception {
        return contactMapper.removeByContactIdAndUserName(map);
    }

    /**
     * 根据想家号码与号码中的联系人号码查找对应的信息
     * @param userName
     * @param contactPhone
     * @return
     * @throws Exception
     */
    @Override
    public Contact findContact(String userName, String contactPhone) throws Exception {
        Map<String, String> map = new HashMap<>();
        if (Util.isEmpty(userName)){
            throw  new RuntimeException("主叫号码不能为空");
        }else {
            userName = "%"+userName+"%";
        }
        if (Util.isEmpty(contactPhone)){
            throw  new RuntimeException("被叫号码不能为空");
        }else {
            contactPhone = "%"+contactPhone+"%";
        }
        map.put("userName",userName);
        map.put("contactPhone",contactPhone);

        List<Contact> contacts = contactMapper.findContact(map);
        if (contacts.size() == 0) {
            return null;
        }
        return contacts.get(0);
    }


}