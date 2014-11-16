package org.fi.muni.diploma.thesis.frontend.navigator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.fi.muni.diploma.thesis.frontend.views.MainView;
import org.fi.muni.diploma.thesis.frontend.views.LoginView;
import org.fi.muni.diploma.thesis.utils.jbpm.RuntimeEngineWrapper;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("mytheme")
public class NavigatorUI extends UI {

	private final static Logger logger = Logger.getLogger(NavigatorUI.class.getName());

	@WebServlet(urlPatterns = "/*", asyncSupported = true, initParams = { @WebInitParam(name = "widgetset", value = "org.fi.muni.diploma.thesis.frontend.AppWidgetSet") })
	@VaadinServletConfiguration(productionMode = false, ui = NavigatorUI.class)
	public static class Servlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;

	}

	private Navigator navigator;

	private static final long serialVersionUID = 511085335415683713L;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Service Lifecycle Management Application");

		// Create a navigator to control the views
		navigator = new Navigator(this, this);

		// Create and register the views
		navigator.addView(LoginView.NAME, new LoginView(navigator));
		navigator.addView(MainView.NAME, new MainView(navigator));
		this.setNavigator(navigator);

		

		// We use a view change handler to ensure the user is always redirected
		// to the login view if the user is not logged in.
		//
		getNavigator().addViewChangeListener(new ViewChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {

				// Check if a user has logged in
				boolean isLoggedIn = getSession().getAttribute("username") != null;
				boolean isLoginView = event.getNewView() instanceof LoginView;

				if (!isLoggedIn && !isLoginView) {
					// Redirect to login view always if a user has not yet
					// logged in
					getNavigator().navigateTo(LoginView.NAME);
					return false;

				} else if (isLoggedIn && isLoginView) {
					// If someone tries to access to login view while logged in,
					// then cancel
					return false;
				}

				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});

	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

}