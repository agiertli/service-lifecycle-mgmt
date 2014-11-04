package org.fi.muni.diploma.thesis.frontend.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/** A start view for navigating to the main view */
public class StartView extends VerticalLayout implements View {
	private static final long serialVersionUID = -3398565663865641952L;
	
    private Navigator navigator = null;
    public final static String NAME = "";


	public StartView(Navigator navigator) {
		this.setNavigator(navigator);
		setSizeFull();

		Button button = new Button("Enter", new Button.ClickListener() {
			private static final long serialVersionUID = -1809072471885383781L;

			@Override
			public void buttonClick(ClickEvent event) {
				StartView.this.navigator.navigateTo(MainView.NAME);
			}
		});
		addComponent(button);
		setComponentAlignment(button, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Service lifecycle manager");
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}
}