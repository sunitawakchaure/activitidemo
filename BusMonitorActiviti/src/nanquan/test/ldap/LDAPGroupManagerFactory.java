package nanquan.test.ldap;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupManager;

public class LDAPGroupManagerFactory implements SessionFactory {

	private LDAPConnectionParams connectionParams;
	
	public LDAPGroupManagerFactory(LDAPConnectionParams params) {
		this.connectionParams = params;
	}
	
	@Override
  public Class<?> getSessionType() {
	  return GroupManager.class;
  }

	@Override
  public Session openSession() {
	  return new LDAPGroupManager(connectionParams);
  }

}
