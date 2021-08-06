//package com.zouyujie.micoder;
//
//import com.zouyujie.micoder.dao.ProductDao;
//import com.zouyujie.micoder.service.ProductService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.Date;
//import java.text.SimpleDateFormat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = MicoderApplication.class)
//public class MicoderApplicationTests implements ApplicationContextAware{
//
//	private ApplicationContext applicationContext;
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContext = applicationContext;
//	}
//
//	@Test
//	public void test(){
//		ProductDao pd = applicationContext.getBean(ProductDao.class);
//		System.out.println(pd.select());
//
//		pd = (ProductDao) applicationContext.getBean("ProductDaoHibernate");
//		System.out.println(pd.select());
//	}
//	@Test
//	public void ServiceTest(){
//		ProductService ps = applicationContext.getBean(ProductService.class);
//		System.out.println(ps);
//	}
//	@Test
//	public void ConfigTest(){
//		SimpleDateFormat sdf = (SimpleDateFormat) applicationContext.getBean("simpleDateFormat");
//		System.out.println(sdf.format(new Date()));
//	}
//	@Autowired
//	private ProductDao productDao;
//	@Test
//	public void TestDI() throws UnknownHostException {
//
//	}
//}
