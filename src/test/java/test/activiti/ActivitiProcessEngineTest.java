package test.activiti;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
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
//        processEngineConfiguration.setJdbcPassword("");

        // 设置数据库建表策略
        /**
         * DB_SCHEMA_UPDATE_TRUE：如果不存在表就创建表，存在就直接使用
         * DB_SCHEMA_UPDATE_FALSE：如果不存在表就抛出异常
         * DB_SCHEMA_UPDATE_CREATE_DROP：每次都先删除表，再创建新的表
         */
//    	processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
//
//        // 3.使用配置对象创建流程引擎实例（检查数据库连接等环境信息是否正确）
//        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
//
//        System.out.println(processEngine);
    }
    
    @Before
    public void init() {
        Session s = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory,
                new SessionHolder(s));
    }
}
