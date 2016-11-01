/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 */
package test.eryansky.service;

import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eryansky.modules.activity.entity.ActProcessType;
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
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Before
    public void init() {
        Session s = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory,
                new SessionHolder(s));
    }
    
    @After
    public void close() {
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                .getResource(sessionFactory);
        SessionFactoryUtils.closeSession(holder.getSession());
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }
    
    @Test
    public void test() { 
    	
    	ActProcessType actProcessType = actProcessTypeManager.getById(new Long(1));
    	if(actProcessType ==null){
    		actProcessType = new ActProcessType();
    		actProcessType.setId(new Long(1));
        	actProcessType.setName("财务管理类");
        	actProcessType.setCode("FinaceActProcessType");
        	actProcessType.setOrderNo(1);
        	actProcessType.setStatus(1);
        	actProcessType.setCreateUser("admin");
        	actProcessType.setCreateTime(new Date());
        	actProcessTypeManager.saveOrUpdate(actProcessType);
    	}
    	
    	actProcessType = actProcessTypeManager.getById(new Long(2));
    	if(actProcessType ==null){
    		actProcessType = new ActProcessType();
        	actProcessType.setId(new Long(2));
        	actProcessType.setName("行政管理类");
        	actProcessType.setCode("ExecActProcessType");
        	actProcessType.setOrderNo(2);
        	actProcessType.setStatus(1);
        	actProcessType.setCreateUser("admin");
        	actProcessType.setCreateTime(new Date());
        	actProcessTypeManager.saveOrUpdate(actProcessType);
    	}
    	
    	actProcessType = actProcessTypeManager.getById(new Long(3));
    	if(actProcessType ==null){
    		actProcessType = new ActProcessType();
        	actProcessType.setId(new Long(3));
        	actProcessType.setName("业务管理类");
        	actProcessType.setCode("BusActProcessType");
        	actProcessType.setOrderNo(3);
        	actProcessType.setStatus(1);
        	actProcessType.setCreateUser("admin");
        	actProcessType.setCreateTime(new Date());
        	actProcessTypeManager.saveOrUpdate(actProcessType);
    	}
    	
    	actProcessType = actProcessTypeManager.getById(new Long(4));
    	if(actProcessType ==null){
    		actProcessType = new ActProcessType();
        	actProcessType.setId(new Long(4));
        	actProcessType.setName("仓管类别");
        	actProcessType.setCode("WareHouseActProcessType");
        	actProcessType.setOrderNo(4);
        	actProcessType.setStatus(1);
        	actProcessType.setCreateUser("admin");
        	actProcessType.setCreateTime(new Date());
        	actProcessTypeManager.saveOrUpdate(actProcessType);
    	} 
    	
    	actProcessType = actProcessTypeManager.getById(new Long(5));
    	if(actProcessType ==null){
    		actProcessType = new ActProcessType();
        	actProcessType.setId(new Long(5));
        	actProcessType.setName("其他类别");
        	actProcessType.setCode("OtherActProcessType");
        	actProcessType.setOrderNo(5);
        	actProcessType.setStatus(1);
        	actProcessType.setCreateUser("admin");
        	actProcessType.setCreateTime(new Date());
        	actProcessTypeManager.saveOrUpdate(actProcessType);
    	}
    }

}
