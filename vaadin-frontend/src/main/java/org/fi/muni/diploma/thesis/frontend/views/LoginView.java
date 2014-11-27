package org.fi.muni.diploma.thesis.frontend.views;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.jbpm.RuntimeEngineWrapper;
import org.fi.muni.diploma.thesis.utils.properties.ApplicationUserRoleProperties;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Login view allows user to enter the application after entering correct credentials
 * 
 * @author osiris
 * 
 */
@SuppressWarnings("unused")
public class LoginView extends VerticalLayout implements View {
	private static final long serialVersionUID = -3398565663865641952L;

	private Navigator navigator = null;
	public final static String NAME = "";
	private final TextField user;

	private final PasswordField password;

	private final Button loginButton;

	private final static Logger logger = Logger.getLogger(LoginView.class.getName());
	private final Label verticalGap = new Label("</br>", ContentMode.HTML);

	public LoginView(Navigator navigator) {
		this.setNavigator(navigator);

		// Create the user input field
		user = new TextField("");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("username");
		user.setInvalidAllowed(false);

		// Create the password input field
		password = new PasswordField("");
		password.setWidth("300px");
		password.addValidator(new PasswordValidator());
		password.setRequired(true);
		password.setInputPrompt("password");
		password.setNullRepresentation("");

		// Create login button
		loginButton = new Button("Enter");
		loginButton.addClickListener(new MyButtonListener());

		Label greeting = new Label();
		greeting.setSizeUndefined();
		greeting.setValue("Service Lifecycle Manager");
		greeting.setStyleName("h1");

		VerticalLayout fields = new VerticalLayout(greeting, user, password, verticalGap, loginButton);
		fields.addStyleName("mynorequiredindicator");

		fields.setComponentAlignment(greeting, Alignment.MIDDLE_CENTER);
		fields.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
		fields.setComponentAlignment(user, Alignment.MIDDLE_CENTER);
		fields.setComponentAlignment(password, Alignment.MIDDLE_CENTER);

		addComponent(fields);
		setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		setSizeFull();

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	// Validator for validating the passwords
	private static final class PasswordValidator extends AbstractValidator<String> {

		private static final long serialVersionUID = 1L;

		public PasswordValidator() {
			super("The password provided is not valid");
		}

		@Override
		protected boolean isValidValue(String value) {
			//
			// Password must be at least 8 characters long and contain at least
			// one number
			//
			if (value != null && (value.length() < 8 || !value.matches(".*\\d.*"))) {
				return false;
			}
			return true;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}
	}

	class MyButtonListener implements Button.ClickListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			//
			// Validate the fields using the navigator. By using validators for the
			// fields we reduce the amount of queries we have to use to the database
			// for wrongly entered passwords

			Boolean isValid = false;
			ApplicationUserRoleProperties props = null;
			try {
				props = new ApplicationUserRoleProperties();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//
			if (!user.isValid() || !password.isValid()) {
				return;
			}

			String username = LoginView.this.user.getValue();
			String password = LoginView.this.password.getValue();
			String roles = "";

			try {

				String securityString = username + ":ApplicationRealm:" + password;

				String responseString = LoginView.this.getMD5Hash(securityString);

				roles = props.getRoles(username);

				if (props.getPassword(username).equals(responseString)) {

					isValid = true;

				}

			} catch (Exception e) {

			}

			if (isValid) {

				// Store the current user in the service session
				getSession().setAttribute("username", username);
				getSession().setAttribute("role", roles);

				try {
					RuntimeEngineWrapper.getInstance(username, password);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Navigate to main view

				if (props.hasRole(username, "SOAGovernanceSpecialist")) {

					getUI().getNavigator().navigateTo(MainView.NAME);
				}

				else {

					getUI().getNavigator().navigateTo(LimitedView.NAME);
				}

			} else {

				// Wrong password clear the password field and refocuses it
				LoginView.this.password.setValue(null);
				LoginView.this.password.focus();

			}
		}

	}

	/**
	 * 
	 * @param securityString
	 *            - username:realm:password
	 * @return HEX(MD5(username:realm:password))
	 * @throws NoSuchAlgorithmException
	 */
	private String getMD5Hash(String securityString) throws NoSuchAlgorithmException {

		MessageDigest m = MessageDigest.getInstance("MD5");
		m.reset();
		m.update(securityString.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}

		return hashtext;
	}

}
