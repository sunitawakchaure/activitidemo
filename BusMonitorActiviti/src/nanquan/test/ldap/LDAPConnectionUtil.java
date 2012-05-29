package nanquan.test.ldap;

import org.activiti.engine.ActivitiException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

public class LDAPConnectionUtil {

	public static LdapConnection openConnection(LDAPConnectionParams connectionParams) {
		LdapConnection connection = new LdapNetworkConnection(connectionParams.getLdapServer(), connectionParams.getLdapPort());
	  try {
	    connection.bind(connectionParams.getLdapUser(), connectionParams.getLdapPassword());
    } catch (Exception e) {
	    throw new ActivitiException("LDAP connection open failure", e);
    }
	  return connection;
	}
	
	public static void closeConnection(LdapConnection connection) {
		try {
	  	connection.unBind();
	  	connection.close();
	  } catch (Exception e) {
	    throw new ActivitiException("LDAP connection close failure", e);
    }
	}
}
