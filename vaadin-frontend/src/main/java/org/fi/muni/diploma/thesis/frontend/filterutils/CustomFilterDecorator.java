package org.fi.muni.diploma.thesis.frontend.filterutils;

import java.io.Serializable;
import java.util.Locale;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.jbpm.ProcessStateMap;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;

public class CustomFilterDecorator implements FilterDecorator, Serializable {

	private static final long serialVersionUID = 3964571205848401790L;
	private final static Logger logger = Logger.getLogger(CustomFilterDecorator.class.getName());

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		
		logger.info("property id:"+propertyId);
		logger.info("value:"+value);
        logger.info("value to string:"+value.toString());
		
		
		if ("Process State".equals(propertyId)) {

			ProcessStateMap.States state = (ProcessStateMap.States) value;
			return ProcessStateMap.getProcessStatusAsString(state);
		}
		else if ("Process Name".equals(propertyId)) {
			
			return value.toString();
		}

		return null;
	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		return null;
	}

	@Override
	public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		return null;
	}

	@Override
	public boolean isTextFilterImmediate(Object propertyId) {
		return false;
	}

	@Override
	public int getTextChangeTimeout(Object propertyId) {
		return 0;
	}

	@Override
	public String getFromCaption() {
		return "From:";
	}

	@Override
	public String getToCaption() {
		return "To:";
	}

	@Override
	public String getSetCaption() {
		return null;
	}

	@Override
	public String getClearCaption() {
		return null;
	}

	@Override
	public Resolution getDateFieldResolution(Object propertyId) {
		return null;
	}

	@Override
	public String getDateFormatPattern(Object propertyId) {
		return null;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public String getAllItemsVisibleString() {
		return "Display all";
	}

	@Override
	public NumberFilterPopupConfig getNumberFilterPopupConfig() {
		return null;
	}

	@Override
	public boolean usePopupForNumericProperty(Object propertyId) {
		if (propertyId.equals("Process Instance ID") || propertyId.equals("Task ID")) {
			
			return true;
		}
		return false;
	}
}
