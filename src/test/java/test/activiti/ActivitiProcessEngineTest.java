package test.activiti;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eryansky.common.model.Datagrid;
import com.eryansky.common.orm.Page;
import com.eryansky.common.utils.jackson.JsonUtil;
import com.eryansky.modules.activity.dto.ProcessDefinitionDto;

/**
 * @author : 尔演&Eryan eryanwcp@gmail.com
 * @date : 2014-07-07 20:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
        "classpath:applicationContext-activiti.xml"  })
public class ActivitiProcessEngineTest {

    private static Logger logger = LoggerFactory.getLogger(ActivitiProcessEngineTest.class);
    
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;
//    @Autowired
//    private ProcessEngine processEngine ;

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Autowired
	protected RepositoryService repositoryService;
	
    
    @After
    public void close() {
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
                .getResource(sessionFactory);
        SessionFactoryUtils.closeSession(holder.getSession());
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }
    
    @Test
    public void test() {
    	// 1.创建Activiti配置对象的实例
//        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
//                .createStandaloneProcessEngineConfiguration();
        // 2.设置数据库连接信息

        // 设置数据库地址
//        processEngineConfiguration
//                .setJdbcUrl("jdbc:mysql://localhost:3306/activiti?createDatabaseIfNotExist&amp;useUnicode=true&amp;characterEncoding=utf8");
        // 数据库驱动
//        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        // 用户名
//        processEngineConfiguration.setJdbcUsername("root");
        // 密码
//        processEngineConfiguration.setJdbcPassword("root");

        // 设置数据库建表策略
        /**
         * DB_SCHEMA_UPDATE_TRUE：如果不存在表就创建表，存在就直接使用
         * DB_SCHEMA_UPDATE_FALSE：如果不存在表就抛出异常
         * DB_SCHEMA_UPDATE_CREATE_DROP：每次都先删除表，再创建新的表
         */
//    	processEngineConfiguration.setDaCtabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
//    	processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
//        // 3.使用配置对象创建流程引擎实例（检查数据库连接等环境信息是否正确）
//        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
//
//        System.out.println(processEngine);
    }
    
    @Test
    public void queryDeployList() {
//    	System.out.println(" queryDeployList Start.");    
//    	Page<Object[]> p = new Page<Object[]>();
//    	ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
// 	    		.latestVersion()
// 	    		.orderByProcessDefinitionKey().asc();
//    	 p.setTotalCount(processDefinitionQuery.count()); 
//    	 System.out.println(" queryDeployList Param:"+JsonUtil.getJson(p) );
//         List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
//         for (ProcessDefinition processDefinition : processDefinitionList) {
//        	System.out.println(" queryDeployList processDefinition:"+processDefinition.getName() );
//        	ProcessDefinitionEntity processDefinitionDB= (ProcessDefinitionEntity)  repositoryService.getProcessDefinition(processDefinition.getId());
//        	logger.info(" processDefinitionDB:{}.",JsonUtil.getJson(processDefinitionDB) ); 
//   	      	String deploymentId = processDefinition.getDeploymentId();
//   	      	DeploymentEntity deploymentDB = (DeploymentEntity)repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
//   	      	logger.info(" deploymentDB:{}.",JsonUtil.getJson(deploymentDB) ); 
//   	      p.getResult().add(new Object[]{processDefinitionDB, deploymentDB});
//   	    } 
//         System.out.println(" queryDeployList End2:"+JsonUtil.getJson(p) ); 
//           Datagrid<Object[]> dg = new Datagrid<Object[]>(p.getTotalCount(), p.getResult());
    }
    
    @Test
    public void queryDeployListbak() {
    	System.out.println(" queryDeployList Start.");    
    	Page<ProcessDefinitionDto> p = new Page<ProcessDefinitionDto>();
    	ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
 	    		.latestVersion()
 	    		.orderByProcessDefinitionKey().asc();
    	 p.setTotalCount(processDefinitionQuery.count()); 
    	 System.out.println(" queryDeployList Param:"+JsonUtil.getJson(p) );
         List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
//         List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(0,15); 
//    	 List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(p.getFirst()-1,15); // p.getMaxResults());
         
//         System.out.println(" queryDeployList End1:"+JsonUtil.getJson(processDefinitionList) ); 
         
         for (ProcessDefinition processDefinition : processDefinitionList) {
        	System.out.println(" queryDeployList processDefinition:"+processDefinition.getName() );
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
	     	System.out.println(" processDefinitionDto Json:"+JsonUtil.getJson(processDefinitionDto) );
        	 p.getResult().add(processDefinitionDto);
//   	      	DeploymentEntity deploymentDB = (DeploymentEntity)repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
//   	      	logger.info(" deploymentDB:{}.",JsonUtil.getJson(deploymentDB) ); 
//   	      DeploymentEntity deploymentDB =  (DeploymentEntity)deployment;
//   	      p.getResult().add(new Object[]{processDefinitionDB, deploymentDB});
   	    } 
//         System.out.println(" queryDeployList End2:"+JsonUtil.getJson(p) ); 
         System.out.println(" treegrid Json:"+JsonUtil.getJson(p) );
 	
           Datagrid<ProcessDefinitionDto> dg = new Datagrid<ProcessDefinitionDto>(p.getTotalCount(), p.getResult());
           System.out.println(" Datagrid Json:"+JsonUtil.getJson(dg) );
//           System.out.println(" queryDeployList End3:"+JsonUtil.getJson(dg) ); 
    }
    
    @Before
    public void init() {
        Session s = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory,
                new SessionHolder(s));
    }
}
