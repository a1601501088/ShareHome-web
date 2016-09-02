package com.vunke.shareHome.service.impl;

import com.vunke.shareHome.dao.TemplateTextMapper;
import com.vunke.shareHome.model.TemplateText;
import com.vunke.shareHome.service.TemplateTextService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/30.
 */
@Service("TemplateTextServ")
public class TemplateTextServiceImpl implements TemplateTextService{
    @Resource
    private TemplateTextMapper templateTextMapper;
    @Override
    public TemplateText findTemplate(String titile, String actionType) throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("title",titile);
        map.put("actionType",actionType);
        List<TemplateText> templateTexts = templateTextMapper.findTemplate(map);
        if (templateTexts.size()==0){
            return null;
        }
        return templateTexts.get(0);
    }
}
