package nanquan.test.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupManager;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.shared.ldap.model.cursor.EntryCursor;
import org.apache.directory.shared.ldap.model.entry.Attribute;
import org.apache.directory.shared.ldap.model.entry.Entry;
import org.apache.directory.shared.ldap.model.message.SearchScope;

public class LDAPGroupManager extends GroupManager {

//	private static final String USER_ENTRY = "ou=users,ou=system";
//	private static final String GROUP_ENTRY = "ou=groups,ou=system";
	
	private static final String USER_ENTRY = "dc=users,DC=ITS";
	private static final String GROUP_ENTRY = "dc=groups,DC=ITS";

	private LDAPConnectionParams connectionParams;

	public LDAPGroupManager(LDAPConnectionParams params) {
		this.connectionParams = params;
	}

	@Override
	public Group createNewGroup(String groupId) {
		throw new ActivitiException(
				"LDAP group manager doesn't support creating a new group");
	}

	@Override
	public void insertGroup(Group group) {
		throw new ActivitiException(
				"LDAP group manager doesn't support inserting a new group");
	}

	@Override
	public void updateGroup(Group updatedGroup) {
		throw new ActivitiException(
				"LDAP group manager doesn't support updating a new group");
	}

	@Override
	public void deleteGroup(String groupId) {
		throw new ActivitiException(
				"LDAP group manager doesn't support deleting a new group");
	}

	@Override
	public List<Group> findGroupByQueryCriteria(Object query, Page page) {
		List<Group> groupList = new ArrayList<Group>();

		// Query is a GroupQueryImpl instance
		GroupQueryImpl groupQuery = (GroupQueryImpl) query;
		StringBuilder searchQuery = new StringBuilder();
		if (StringUtils.isNotEmpty(groupQuery.getId())) {
			searchQuery.append("(cn=").append(groupQuery.getId()).append(")");

		} else if (StringUtils.isNotEmpty(groupQuery.getName())) {
			searchQuery.append("(cn=").append(groupQuery.getName()).append(")");

		} else if (StringUtils.isNotEmpty(groupQuery.getUserId())) {
			searchQuery.append("(uniqueMember= uid=")
					.append(groupQuery.getUserId())
					.append("," + USER_ENTRY + ")");

		} else {
			searchQuery.append("(cn=*)");
		}
		LdapConnection connection = LDAPConnectionUtil
				.openConnection(connectionParams);
		try {
			EntryCursor search = connection.search(GROUP_ENTRY,
					searchQuery.toString(), SearchScope.ONELEVEL, "*");

			while (search.next()) {
				Group group = new GroupEntity();
				Entry entry = search.get();
				Collection<Attribute> attributes = entry.getAttributes();
				for (Attribute attribute : attributes) {
					String key = attribute.getId();
					if ("cn".equalsIgnoreCase(key)) {
						group.setId(attribute.getString());
						group.setName(attribute.getString());
					}
				}
				groupList.add(group);
			}

			search.close();

		} catch (Exception e) {
			throw new ActivitiException("LDAP connection search failure", e);
		}

		LDAPConnectionUtil.closeConnection(connection);

		return groupList;
	}

	@Override
	public long findGroupCountByQueryCriteria(Object query) {
		return findGroupByQueryCriteria(query, null).size();
	}

	@Override
	public GroupEntity findGroupById(String groupId) {
		throw new ActivitiException(
				"LDAP group manager doesn't support finding a group by id");
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
		List<Group> groupList = new ArrayList<Group>();

		LdapConnection connection = LDAPConnectionUtil
				.openConnection(connectionParams);
		try {
			EntryCursor search = connection.search(GROUP_ENTRY,
					"(uniqueMember= uid=" + userId + "," + USER_ENTRY + ")",
					SearchScope.ONELEVEL, "*");

			while (search.next()) {
				Group group = new GroupEntity();
				Entry entry = search.get();
				Collection<Attribute> attributes = entry.getAttributes();
				for (Attribute attribute : attributes) {
					String key = attribute.getId();
					if ("cn".equalsIgnoreCase(key)) {
						group.setId(attribute.getString());
						group.setName(attribute.getString());
					}
				}
				groupList.add(group);
			}

			search.close();

		} catch (Exception e) {
			throw new ActivitiException("LDAP connection search failure", e);
		}

		LDAPConnectionUtil.closeConnection(connection);

		return groupList;
	}
}
