/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 */
package test.eryansky.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eryansky.modules.activity.service.ActProcessTypeManager;

/**
 * @author : 尔演&Eryan eryanwcp@gmail.com
 * @date : 2014-07-12 22:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-mybatis.xml"})
public class ActProcessMybatisTest {

    @Autowired
    private ActProcessTypeManager  actProcessTypeManager;

    @Test
    public void initParActProcessType() {
//    	actProcessTypeManager.deleteById(new Long(1));
//    	ActProcessType actProcessType = new ActProcessType();
//    	actProcessType.setId(new Long(1));
//    	actProcessType.setName("财务管理类");
//    	actProcessType.setCode("FinaceActProcessType");
//    	actProcessType.setOrderNo(1);
//    	actProcessType.setStatus(1);
//    	actProcessType.setCreateUser("admin");
//    	actProcessType.setCreateTime(new Date());
//    	actProcessTypeManager.saveOrUpdate(actProcessType);
//    	
//    	actProcessType.setId(new Long(2));
//    	actProcessType.setName("行政管理类");
//    	actProcessType.setCode("ExecActProcessType");
//    	actProcessType.setOrderNo(2);
//    	actProcessType.setStatus(1);
//    	actProcessType.setCreateUser("admin");
//    	actProcessType.setCreateTime(new Date());
//    	actProcessTypeManager.saveOrUpdate(actProcessType);
//
//    	actProcessType.setId(new Long(3));
//    	actProcessType.setName("业务管理类");
//    	actProcessType.setCode("BusActProcessType");
//    	actProcessType.setOrderNo(3);
//    	actProcessType.setStatus(1);
//    	actProcessType.setCreateUser("admin");
//    	actProcessType.setCreateTime(new Date());
//    	actProcessTypeManager.saveOrUpdate(actProcessType);
//
//    	actProcessType.setId(new Long(4));
//    	actProcessType.setName("仓管类别");
//    	actProcessType.setCode("WareHouseActProcessType");
//    	actProcessType.setOrderNo(3);
//    	actProcessType.setStatus(1);
//    	actProcessType.setCreateUser("admin");
//    	actProcessType.setCreateTime(new Date());
//    	actProcessTypeManager.saveOrUpdate(actProcessType);
//
//    	actProcessType.setId(new Long(5));
//    	actProcessType.setName("其他类别");
//    	actProcessType.setCode("OtherActProcessType");
//    	actProcessType.setOrderNo(4);
//    	actProcessType.setStatus(1);
//    	actProcessType.setCreateUser("admin");
//    	actProcessType.setCreateTime(new Date());
//    	actProcessTypeManager.saveOrUpdate(actProcessType);
    }
    
}
