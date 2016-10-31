/**
 *  Copyright (c) 2014 http://www.jfit.com.cn
 *
 *          江西省锦峰软件科技有限公司
 */
package com.eryansky.modules.activity.web;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eryansky.common.model.Datagrid;
import com.eryansky.common.orm.Page;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateWebUtils;
import com.eryansky.common.web.springmvc.BaseController;
import com.eryansky.common.web.springmvc.SpringMVCHolder;
import com.eryansky.modules.activity.entity.ActProcessType;
import com.eryansky.modules.activity.service.ActDeployManager;

/**
 * 流程 管理 --流程部署
 * 包含：流程的管理  流程部署 
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/act/actprocessdeploy")
public class ActDeployController  extends BaseController<ActProcessType,Long> {
	
	@Autowired
	private ActDeployManager actDeployManager;

	@Autowired
	protected RepositoryService repositoryService;
	
	@Override
	public EntityManager<ActProcessType, Long> getEntityManager() {
		return actDeployManager;
	}
	


	 @Override
    @RequestMapping(value = {"datagrid"})
    @ResponseBody
    public Datagrid datagrid() {
        // 自动构造属性过滤器
        List<PropertyFilter> filters = HibernateWebUtils.buildPropertyFilters(SpringMVCHolder.getRequest());
        Page<Object[]> p = new Page<Object[]>(SpringMVCHolder.getRequest());
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().orderByProcessDefinitionKey().asc();
	    String category="";
	    if (StringUtils.isNotEmpty(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
	    p.setTotalCount(processDefinitionQuery.count());
	    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(p.getFirst(), p.getMaxResults());

	    for (ProcessDefinition processDefinition : processDefinitionList) {
	      String deploymentId = processDefinition.getDeploymentId();
	      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	      p.getResult().add(new Object[]{processDefinition, deployment});
	    } 
        Datagrid<Object[]> dg = new Datagrid<Object[]>(p.getTotalCount(), p.getResult());
        return dg;
    }




}