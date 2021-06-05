package com.liveinpast.community;

import com.liveinpast.community.Dao.AlphaDao;
import com.liveinpast.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test for IOC container:
 * 		ApplicationContext is the sub-interface of BeanFactory, BeanFactory manage all the Beans. If we want to get and
 * 	control beans, implement ApplicationContextAware and get the applicationContext.
 *
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * Procedure 1:
	 * get beans and store in variable applicationContext.
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Procedure 2:
	 * print applicationContext
	 * result: org.springframework.web.context.support.GenericWebApplicationContext@1fb669c3, started on Fri Jun 04 18:24:12 CST 2021
	 */
	@Test
	public void TestApplicationContext() {
		System.out.println(applicationContext);
	}

	/**
	 * Procedure 3:
	 * In the previous test, bean is actually exists!
	 * Now the problems are: how can we manage beans?
	 * Solutions:
	 * 		1.create beans to manipulate database: AlphaDao interface store in package Dao(Data access object)
	 * 		2.create methods select() and implement by class AlphaDaoHibernateImpl, return string "Hibernate"
	 * 		3.add annotations above the class: In this case is "@Repository"
	 * result: Hibernate
	 */
	@Test
	public void TestGettingHiberApplicationContext() {
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());
	}

	/**
	 * Procedure 4:
	 * if we add another database manipulation technology Mybatis to project, there are two repositories in the same
	 * interface AlphaDao, which will occur collisions. To solve the problem is to add the annotation "@Primary" above
	 * the targeted bean. The key is a higher priority.
	 * Solutions:
	 * 	1. The create bean procedure is the same as procedure 3
	 * 	2.add "@Primary" above class
	 * Advantages:
	 * 	1.simply manage and control beans even though older bean has been used in a lot of place.
	 * 	2.bean is responsible for interface(interface orientation), therefore the specific class changed will not influence
	 * 	the interface.
	 */
	@Test
	public void TestGettingMybatisApplicationContext() {
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());
	}

	/**
	 * Procedure 5:
	 * In the previous Procedure, add annotation "@Primary" can switch to different bean,which means the primary will
	 * take place of the older bean. What if I would like to use it in the very little piece of code, what should I do?
	 * Solutions: name beans
	 * 	1.the default bean name is low-case class name
	 * 	2.we can change it in "@Repository("")
	 * 	3.use it with name declaration
	 */
	@Test
	public void TestDealingMutilBeansApplicationContext() {
//		input directly beans name will be regarded as an Object,
//		AlphaDao alphaDao = applicationContext.getBean("alphaHibernate");
		AlphaDao alphaDao = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(alphaDao.select());
	}

	/**
	 * Procedure 6:
	 * bean initialization and destroy
	 * Solutions:
	 * 	1.create new bean AlphaService and add annotation "@Service" above
	 * 	2.create init method, add annotation "@PostConstruct": run after constructor
	 * 	3.create destory method, annotation "@PreDestroy" : run before recycling object
	 */
	@Test
	public void TestBeanManagement() {
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	/**
	 * Procedure 7:
	 * beans are singleton: bean only instance once, include init and destroy
	 * if want to change, add "@Scope()" above service
	 * The default value in Scope is singleton, name it with prototype, will create new instance every time you initialize
	 */
	@Test
	public void TestBeanManagements() {
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	/**
	 * Procedure 8:
	 * The above beans are written by us, what if I want to manage a bean from a jar
	 * which means I can't add an annotation casually or change it
	 * Solutions:
	 * create a configuration class, declare bean annotation in that class
	 * 	1.create class named AlphaConfig, add annotation "@Configuration" to declare the type
	 * 	2.create simple date method, add "@Bean" annotation
	 */
	@Test
	public void TestBeanConfig() {
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	/**
	 * Procedure 9:
	 * The aboce procedure are not the key content of DI(Dependency Injection), we do it all by our own.
	 * Solutions:
	 *  use "Autowired" to automatically get and return object
	 *  if I want to inject a specific bean, use "@Qualifier("name")"
	 */
	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;
	@Autowired
	private AlphaService alphaService;
	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI() {
		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}

	/**
	 * Procedure 10:
	 * project logic:
	 * Controller(deal with browser request) ->  service(specific requirement) -> Dao(access database)
	 * dependency Injection:
	 * Controller <-  service  <- Dao
	 */
}
