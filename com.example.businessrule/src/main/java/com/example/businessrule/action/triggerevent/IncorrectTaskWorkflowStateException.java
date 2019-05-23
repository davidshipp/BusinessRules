package com.example.businessrule.action.triggerevent;

import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.framework.localization.Localizable;

@Localizable("Current object is not in state with ID '{stateID}'")
public class IncorrectTaskWorkflowStateException extends BusinessRulePluginException {

	private final String stateID;

	public IncorrectTaskWorkflowStateException(final String stateID) {
		this.stateID = stateID;
	}

	public String getStateID() {
		return stateID;
	}
}
