package org.fi.muni.diploma.thesis.utils.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/** Representation of
 * application-users.properties
 * application-roles.properties
 * 
 * as we know them from JBoss 
 * @author Anton Giertli
 *
 */
public class ApplicationUserRoleProperties {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ApplicationUserRoleProperties.class.getName());

	private Properties userProperties;
	private Properties roleProperties;

	public ApplicationUserRoleProperties() throws IOException {

		
		String dir = System.getProperty("jboss.server.config.dir");

		FileInputStream is = new FileInputStream(dir + "/application-users.properties");
		FileInputStream is2 = new FileInputStream(dir + "/application-roles.properties");

		Properties props = new Properties();
		Properties props2 = new Properties();
		props.load(is);
		props2.load(is2);

		this.userProperties = props;
		this.roleProperties = props2;

		is.close();
		is2.close();
	}

	public String getPassword(String username) {
		return this.userProperties.getProperty(username);
	}

	
	public String getRoles(String user) {
		
		return (String)this.roleProperties.get(user);
	}
	
	/**
	 * Roles are stored in the following format in the properties file:
	 * 
	 * user=role1,role2
	 * 
	 * @param user
	 * @param role
	 * @return true if user has the role assigned in properties file, false otherwise
	 */
	public boolean hasRole(String user, String role) {

		boolean hasRole = false;

		List<String> roles = Arrays.asList(((String) this.roleProperties.get(user)).split(","));

		for (String currentRole : roles) {

			if (currentRole.equals(role)) {

				return true;
			}
		}

		return hasRole;
	}

}
