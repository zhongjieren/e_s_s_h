/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.eryansky.modules.activity.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eryansky.common.model.Datagrid;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateDao;
import com.eryansky.modules.activity.entity.ActProcessType;

/**
 * 流程类型类型实现类.
 */
@Service
public class ActDeployManager extends EntityManager<ActProcessType, Long> {

	private HibernateDao<ActProcessType, Long> ActProcessTypeDao;
	
//	@Autowired
//	private RepositoryService repositoryService;
//	@Autowired
//	private RuntimeService runtimeService;

	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		ActProcessTypeDao = new HibernateDao<ActProcessType, Long>(
				sessionFactory, ActProcessType.class);
	}

	@Override
	protected HibernateDao<ActProcessType, Long> getEntityDao() {
		return ActProcessTypeDao;
	}
	
	
	
	/**
	 * 流程定义列表
	 */
	public Datagrid<Object[]> processList(Datagrid<Object[]> dg, String category) {

//		 List<PropertyFilter> filters = Lists.newArrayList();
//	        List<ActProcessType> p = getEntityManager().find(filters, sort, order);
//	        Datagrid<ActProcessType> dg = new Datagrid<ActProcessType>(p.size(), p);
	       
//	    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
//	    		.latestVersion().orderByProcessDefinitionKey().asc();
//	    
//	    if (StringUtils.isNotEmpty(category)){
//	    	processDefinitionQuery.processDefinitionCategory(category);
//		}
//	    dg.setTotal(processDefinitionQuery.count());
////	    page.setCount(processDefinitionQuery.count());
//	    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(dg., page.getMaxResults());

//	    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getFirstResult(), page.getMaxResults());
//	    for (ProcessDefinition processDefinition : processDefinitionList) {
//	      String deploymentId = processDefinition.getDeploymentId();
//	      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
//	      page.getList().add(new Object[]{processDefinition, deployment});
//	    }
	    return dg;
	}

	
	

}
