package com.vunke.shareHome.service;

import com.vunke.shareHome.model.TemplateText;

/**
 * Created by Administrator on 2016/8/30.
 */
public interface TemplateTextService {
    TemplateText findTemplate(String titile, String actionType) throws Exception;
}
