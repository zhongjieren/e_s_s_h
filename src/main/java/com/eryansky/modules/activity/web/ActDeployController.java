/**
 *  Copyright (c) 2014 http://www.jfit.com.cn
 *
 *          江西省锦峰软件科技有限公司
 */
package com.eryansky.modules.activity.web;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eryansky.common.model.Datagrid;
import com.eryansky.common.model.Result;
import com.eryansky.common.orm.Page;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateWebUtils;
import com.eryansky.common.utils.encode.Encrypt;
import com.eryansky.common.utils.jackson.JsonUtil;
import com.eryansky.common.web.springmvc.BaseController;
import com.eryansky.common.web.springmvc.SpringMVCHolder;
import com.eryansky.modules.activity.dto.ProcessDefinitionDto;
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
	
	 @RequestMapping(value = {""})
    public String list() {
        return modules+"/act/actprocessdeploy";
    }

	 
	 /**
     * 数据列表. 无分页.
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"treegrid"})
    @ResponseBody
    public Datagrid treegrid(String sort, String order) throws Exception {
    	 // 自动构造属性过滤器
        List<PropertyFilter> filters = HibernateWebUtils.buildPropertyFilters(SpringMVCHolder.getRequest());
        Page<ProcessDefinitionDto> p = new Page<ProcessDefinitionDto>(SpringMVCHolder.getRequest());
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().orderByProcessDefinitionKey().asc();
	    String category="";
	    if (StringUtils.isNotEmpty(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
	    logger.info(" treegrid b:{}.",JsonUtil.getJson(p) ); 
	    p.setTotalCount(processDefinitionQuery.count());
	    logger.info(" treegrid a:{}.",JsonUtil.getJson(p) ); 
	    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(p.getFirst()-1, p.getMaxResults());
	    
	    for (ProcessDefinition processDefinition : processDefinitionList) { 
    		String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			
			ProcessDefinitionDto processDefinitionDto = new ProcessDefinitionDto();
			processDefinitionDto.setCategory(processDefinition.getCategory());
			processDefinitionDto.setKey(processDefinition.getKey());
			processDefinitionDto.setDeploymentId(processDefinition.getDeploymentId());
			processDefinitionDto.setDescription(processDefinition.getDescription());
			processDefinitionDto.setResourceName(processDefinition.getResourceName());
			processDefinitionDto.setTenantId(processDefinition.getTenantId());
			processDefinitionDto.setVersion(processDefinition.getVersion());
			processDefinitionDto.setName(processDefinition.getName());
			processDefinitionDto.setDeploymentTime(deployment.getDeploymentTime());
			p.getResult().add(processDefinitionDto);
//	      p.getResult().add(new Object[]{processDefinition, deployment});
	    } 
//	    logger.info(" treegrid:{}.",JsonUtil.getJson(p) ); 
        Datagrid<ProcessDefinitionDto> dg = new Datagrid<ProcessDefinitionDto>(p.getTotalCount(), p.getResult());
        return dg;
    } 
    
    

    @RequestMapping(value = {"input"})
    public String input(@ModelAttribute("model")ActProcessType ActProcessType) {
        return modules+"/act/actprocessdeploy-input";
    }
    
    @RequestMapping(value = {"upload"})
    @ResponseBody
    public Result upload(String category, MultipartFile file) { 
        Result result = null;
        String message = "";
        logger.info("requestParam category:"+category);
		String fileName = file.getOriginalFilename();
		try {
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment = null;
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (fileName.indexOf("bpmn20.xml") != -1) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (extension.equals("bpmn")) { 
				// bpmn扩展名特殊处理，转换为bpmn20.xml
				String baseName = FilenameUtils.getBaseName(fileName); 
				deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			} else {
				message = "不支持的文件类型：" + extension;
			}
			
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

			// 设置流程分类
			for (ProcessDefinition processDefinition : list) {
//					ActUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
				message += "部署成功，流程ID=" + processDefinition.getId() + "<br/>";
			}
			
			if (list.size() == 0){
				message = "部署失败，没有流程。";
				result = new Result(Result.WARN,message, "name");
				return result;
			}
			
		} catch (Exception e) {
			message = "部署失败！"; 
			result = new Result(Result.WARN,message, "name");
			logger.info(message+":"+e.getStackTrace());
			logger.debug(result.toString());
			return result;
		} 
        result = Result.successResult();
        logger.debug(result.toString());
        return result;
    }

}