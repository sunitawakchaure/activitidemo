package nanquan.test;

import nanquan.test.ldap.LDAPConnectionParams;
import nanquan.test.ldap.LDAPGroupManager;
import nanquan.test.ldap.LDAPUserManager;

import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.UserQueryImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class LdapTest {
	
	public static void main(String[] args) {
		String path = "activiti.cfg-mem-ldap.xml";
		Resource resource = new ClassPathResource(path);
		BeanFactory beanFactory = new XmlBeanFactory(resource);
		
		LDAPConnectionParams connectionParams = (LDAPConnectionParams) beanFactory.getBean("ldapConnectionParams");
		LDAPUserManager userManager = new LDAPUserManager(connectionParams);
		LDAPGroupManager groupManager = new LDAPGroupManager(connectionParams);
		
		Boolean checkPassword = userManager.checkPassword("admin", "passw0rd");
		System.out.println(checkPassword);
		
		UserQueryImpl query = new UserQueryImpl();
		query.userId("admin");
		long size = userManager.findUserCountByQueryCriteria(query);
		System.out.println(size);
		
		GroupQueryImpl groupQuery = new GroupQueryImpl();
		groupQuery.groupId("AdminGroup");
		long groupSize = groupManager.findGroupCountByQueryCriteria(groupQuery);
		System.out.println(groupSize);
		
//		Boolean checkPassword = userManager.checkPassword("admin01", "passw0rd");
//		System.out.println(checkPassword);
		
	}

}
