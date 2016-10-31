/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.eryansky.modules.activity.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eryansky.common.model.Combobox;
import com.eryansky.common.model.Datagrid;
import com.eryansky.common.model.Result;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.utils.StringUtils;
import com.eryansky.common.web.springmvc.BaseController;
import com.eryansky.modules.activity.entity.ActProcessType;
import com.eryansky.modules.activity.service.ActProcessTypeManager;
import com.eryansky.utils.SelectType;
import com.google.common.collect.Lists;

/**
 * 流程分类类型ActProcessType管理 Controller层.
 */
@Controller
@RequestMapping(value = "${adminPath}/act/actprocesstype")
public class ActProcessTypeController extends BaseController<ActProcessType,Long> {
	
    @Autowired
    private ActProcessTypeManager actProcessTypeManager;

    @Override
    public EntityManager<ActProcessType, Long> getEntityManager() {
        return actProcessTypeManager;
    }
    
    @RequestMapping(value = {""})
    public String list() {
        return modules+"/act/actprocesstype";
    }
    
    @RequestMapping(value = {"input"})
    public String input(@ModelAttribute("model")ActProcessType ActProcessType) {
        return modules+"/act/actprocesstype-input";
    }


    @RequestMapping(value = {"save"})
    @ResponseBody
    public Result save(@ModelAttribute("model") ActProcessType ActProcessType) {
        getEntityManager().evict(ActProcessType);
        Result result = null;
        ActProcessType.setGroupActProcessType(null);
        ActProcessType groupActProcessType = actProcessTypeManager.getByCode(ActProcessType.getGroupActProcessTypeCode());
        ActProcessType.setGroupActProcessType(groupActProcessType);

        // 名称是否重复校验
        ActProcessType checkActProcessType1 = actProcessTypeManager
                .getByGroupCode_Name(ActProcessType.getGroupActProcessTypeCode(), ActProcessType.getName());
        if (checkActProcessType1 != null
                && !checkActProcessType1.getId().equals(ActProcessType.getId())) {
            result = new Result(Result.WARN, "分组[" + groupActProcessType.getName() + "]名称为["
                    + ActProcessType.getName() + "]已存在,请修正!", "name");
            return result;
        }

        // 编码是否重复校验
        ActProcessType checkActProcessType3 = actProcessTypeManager.getByCode(ActProcessType.getCode());
        if (checkActProcessType3 != null
                && !checkActProcessType3.getId().equals(ActProcessType.getId())) {
            result = new Result(Result.WARN, "编码为["
                    + ActProcessType.getCode() + "]已存在,请修正!", "code");
            logger.debug(result.toString());
            return result;
        }
        //修改操作 避免自关联数据的产生
        if (ActProcessType.getId() != null) {
            if (ActProcessType.getCode().equals(ActProcessType.getGroupActProcessTypeCode())) {
                result = new Result(Result.ERROR, "不允许发生自关联.",
                        null);
                logger.debug(result.toString());
                return result;
            }
        }

        actProcessTypeManager.saveEntity(ActProcessType);
        result = Result.successResult();
        logger.debug(result.toString());
        return result;
    }


    /**
     * 下拉列表
     */
    @RequestMapping(value = {"combobox"})
    @ResponseBody
    public List<Combobox> combobox(String selectType) throws Exception {
        List<ActProcessType> list = actProcessTypeManager.getGroupActProcessTypes();
        List<Combobox> cList = Lists.newArrayList();

        //为combobox添加  "---全部---"、"---请选择---"
        if (!StringUtils.isBlank(selectType)) {
            SelectType s = SelectType.getSelectTypeValue(selectType);
            if (s != null) {
                Combobox selectCombobox = new Combobox("", s.getDescription());
                cList.add(selectCombobox);
            }
        }
        for (ActProcessType d : list) {
            List<ActProcessType> subActProcessTypes = d.getSubActProcessTypes();
            for (ActProcessType subActProcessType : subActProcessTypes) {
                Combobox combobox = new Combobox(subActProcessType.getCode(), subActProcessType.getName(), d.getName());
                cList.add(combobox);
            }

        }
        return cList;
    }

    /**
     * 分组下拉列表
     */
    @RequestMapping(value = {"group_combobox"})
    @ResponseBody
    public List<Combobox> group_combobox(String selectType) throws Exception {
        List<ActProcessType> list = actProcessTypeManager.getGroupActProcessTypes();
        List<Combobox> cList = Lists.newArrayList();

        //为combobox添加  "---全部---"、"---请选择---"
        if (!StringUtils.isBlank(selectType)) {
            SelectType s = SelectType.getSelectTypeValue(selectType);
            if (s != null) {
                Combobox selectCombobox = new Combobox("", s.getDescription());
                cList.add(selectCombobox);
            }
        }
        for (ActProcessType d : list) {
            Combobox combobox = new Combobox(d.getCode(), d.getName());
            cList.add(combobox);
        }
        return cList;
    }

    /**
     * 排序最大值.
     */
    @RequestMapping(value = {"maxSort"})
    @ResponseBody
    public Result maxSort() throws Exception {
        Integer maxSort = actProcessTypeManager.getMaxSort();
        Result result = new Result(Result.SUCCESS, null, maxSort);
        return result;
    }

    /**
     * 数据列表. 无分页.
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"treegrid"})
    @ResponseBody
    public Datagrid<ActProcessType> treegrid(String sort, String order) throws Exception {
        List<PropertyFilter> filters = Lists.newArrayList();
        List<ActProcessType> p = getEntityManager().find(filters, sort, order);
        Datagrid<ActProcessType> dg = new Datagrid<ActProcessType>(p.size(), p);
        return dg;
    }


}
