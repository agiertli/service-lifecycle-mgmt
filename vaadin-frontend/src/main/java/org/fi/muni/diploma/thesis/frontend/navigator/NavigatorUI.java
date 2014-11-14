package org.fi.muni.diploma.thesis.frontend.navigator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;

import org.fi.muni.diploma.thesis.frontend.views.MainView;
import org.fi.muni.diploma.thesis.frontend.views.StartView;
import org.fi.muni.diploma.thesis.utils.RuntimeEngineWrapper;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("mytheme")
// BEGIN-EXAMPLE: advanced.navigator.basic
public class NavigatorUI extends UI {

	private final static Logger logger = Logger.getLogger(NavigatorUI.class.getName());

	//@WebServlet(value = "/*", asyncSupported = true)
	@WebServlet(urlPatterns = "/*",  asyncSupported = true, initParams = { @WebInitParam(name = "widgetset", value = "org.fi.muni.diploma.thesis.frontend.AppWidgetSet") })
	@VaadinServletConfiguration(productionMode = false, ui = NavigatorUI.class)
	public static class Servlet extends VaadinServlet {

		private static final long serialVersionUID = 1L;

	}
	
	

	private Navigator navigator;

	private static final long serialVersionUID = 511085335415683713L;

	protected static final String MAINVIEW = "main";
	protected static final String STARTVIEW = "start";

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Service Lifecycle Management Application");

		// Create a navigator to control the views
		navigator = new Navigator(this, this);

		// Create and register the views
		navigator.addView(StartView.NAME, new StartView(navigator));
		navigator.addView(MainView.NAME, new MainView(navigator));
		this.setNavigator(navigator);

		// we won't need to call this anymore
		try {
			
		//	logger.info("obtaining remote runtime engine wrapper");

			RuntimeEngineWrapper.getInstance();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

}
// END-EXAMPLE: advanced.navigator.basic