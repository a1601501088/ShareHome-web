package com.vunke.shareHome.dao;

import com.vunke.shareHome.model.TemplateText;

import java.util.List;
import java.util.Map;

public interface TemplateTextMapper {
    int deleteByPrimaryKey(String id);

    int insert(TemplateText record);

    int insertSelective(TemplateText record);

    TemplateText selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TemplateText record);

    int updateByPrimaryKey(TemplateText record);

    List<TemplateText> findTemplate(Map<String,String> map);
}