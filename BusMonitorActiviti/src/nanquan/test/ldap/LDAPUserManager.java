package nanquan.test.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserManager;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.shared.ldap.model.cursor.EntryCursor;
import org.apache.directory.shared.ldap.model.entry.Attribute;
import org.apache.directory.shared.ldap.model.entry.Entry;
import org.apache.directory.shared.ldap.model.message.BindRequestImpl;
import org.apache.directory.shared.ldap.model.message.BindResponse;
import org.apache.directory.shared.ldap.model.message.ResultCodeEnum;
import org.apache.directory.shared.ldap.model.message.SearchScope;
import org.apache.directory.shared.ldap.model.name.Dn;

public class LDAPUserManager extends UserManager {

//	private static final String USER_GROUP = "ou=users,ou=system";
	private static final String USER_GROUP = "dc=users,DC=ITS";

	private LDAPConnectionParams connectionParams;

	public LDAPUserManager(LDAPConnectionParams params) {
		this.connectionParams = params;
	}

	@Override
	public User createNewUser(String userId) {
		throw new ActivitiException(
				"LDAP user manager doesn't support creating a new user");
	}

	@Override
	public void insertUser(User user) {
		throw new ActivitiException(
				"LDAP user manager doesn't support inserting a new user");
	}

	@Override
	public void updateUser(User updatedUser) {
		throw new ActivitiException(
				"LDAP user manager doesn't support updating a user");
	}

	@Override
	public UserEntity findUserById(String userId) {
		throw new ActivitiException(
				"LDAP user manager doesn't support finding a user by id");
	}

	@Override
	public void deleteUser(String userId) {
		throw new ActivitiException(
				"LDAP user manager doesn't support deleting a user");
	}

	@Override
	public List<User> findUserByQueryCriteria(Object query, Page page) {

		List<User> userList = new ArrayList<User>();

		// Query is a UserQueryImpl instance
		UserQueryImpl userQuery = (UserQueryImpl) query;
		StringBuilder searchQuery = new StringBuilder();
		if (StringUtils.isNotEmpty(userQuery.getId())) {
			searchQuery.append("(uid=").append(userQuery.getId()).append(")");

		} else if (StringUtils.isNotEmpty(userQuery.getLastName())) {
			searchQuery.append("(sn=").append(userQuery.getLastName())
					.append(")");

		} else {
			searchQuery.append("(uid=*)");
		}
		LdapConnection connection = LDAPConnectionUtil
				.openConnection(connectionParams);
		try {
			EntryCursor search = connection.search(USER_GROUP,
					searchQuery.toString(), SearchScope.ONELEVEL, "*");
			while (search.next()) {
				User user = new UserEntity();
				Entry entry = search.get();
				Collection<Attribute> attributes = entry.getAttributes();
				for (Attribute attribute : attributes) {
					String key = attribute.getId();
					if ("uid".equalsIgnoreCase(key)) {
						user.setId(attribute.getString());

					} else if ("sn".equalsIgnoreCase(key)) {
						user.setLastName(attribute.getString());

					} else if ("cn".equalsIgnoreCase(key)) {
						user.setFirstName(attribute.getString().replace("cn:", "").trim());
//						user.setFirstName(attribute.getString().substring(0,
//								attribute.getString().indexOf(" ")));
					}
				}
				userList.add(user);
			}

			search.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ActivitiException("LDAP connection search failure", e);
		}

		LDAPConnectionUtil.closeConnection(connection);

		return userList;
	}

	@Override
	public long findUserCountByQueryCriteria(Object query) {
		return findUserByQueryCriteria(query, null).size();
	}

	@Override
	public Boolean checkPassword(String userId, String password) {
		boolean credentialsValid = false;
		LdapNetworkConnection connection = new LdapNetworkConnection(
				connectionParams.getLdapServer(),
				connectionParams.getLdapPort());
		try {
//			connection.bind("uid=" + userId + ","
//					+ USER_GROUP, password);
			BindRequestImpl bindRequest = new BindRequestImpl();
			Dn dn = new Dn("uid=" + userId + ","
					+ USER_GROUP);
			bindRequest.setDn(dn );
			bindRequest.setCredentials(password);
			BindResponse response = connection.bind(bindRequest);
			if (response.getLdapResult().getResultCode() == ResultCodeEnum.SUCCESS) {
				credentialsValid = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ActivitiException("LDAP connection bind failure", e);
		}

		LDAPConnectionUtil.closeConnection(connection);

		return credentialsValid;
	}
}
