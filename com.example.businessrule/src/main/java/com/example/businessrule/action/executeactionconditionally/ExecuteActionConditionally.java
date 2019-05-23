package com.example.businessrule.action.executeactionconditionally;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.stibo.core.domain.Node;
import com.stibo.core.domain.businessrule.BusinessAction;
import com.stibo.core.domain.businessrule.BusinessCondition;
import com.stibo.core.domain.businessrule.evaluate.BusinessActionResult;
import com.stibo.core.domain.businessrule.evaluate.BusinessConditionResult;
import com.stibo.core.domain.businessrule.evaluate.BusinessRuleException;
import com.stibo.core.domain.businessrule.plugin.BusinessActionPlugin;
import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.framework.Plugin;
import com.stibo.framework.PluginName;
import com.stibo.framework.PluginParameter;
import com.stibo.framework.Required;

/**
 * This example shows how to evaluate business conditions and execute business actions using the
 * STEP Extension API.
 * <p>
 * The action will be executed only if the supplied condition evaluates to true or if no condition is supplied.
 */
@Plugin(id = "com.example.businessrule.action.executeactionconditionally.ExecuteActionConditionally")
@PluginName("Execute Action Conditionally")
public class ExecuteActionConditionally implements BusinessActionPlugin<ExecuteActionConditionally.Parameters, ExecuteActionConditionally.Context> {

	private BusinessAction action;
	private BusinessCondition condition;
	
	public interface Parameters {

		@PluginParameter(name = "Business condition", description = "Optional condition for executing the configured business action")
		BusinessCondition getBusinessCondition();

		@Required
		@PluginParameter(name = "Business action", description = "Business action to execute")
		BusinessAction getBusinessAction();
	}

	public interface Context {

		Logger getLogger();
		Node getCurrentObject();
	}

    @Override
    public void executeAction(Context context) throws BusinessRulePluginException {
	    Node node = context.getCurrentObject();
	    Logger logger = context.getLogger();

	    if (condition == null || shouldExecuteBusinessAction(node, logger)) {
            executeBusinessAction(node, logger);
        }
    }

	@Override
	public void initFromConfiguration(Parameters parameters) {
		action = parameters.getBusinessAction();
		condition = parameters.getBusinessCondition();
	}

	@Override
	public Object getDescription() {
		return "Business action that executes another action if supplied condition evaluates to true or no condition is defined";
	}

	private boolean shouldExecuteBusinessAction(Node node, Logger logger) throws BusinessRulePluginException {
		BusinessConditionResult conditionResult;
		try {
			conditionResult = condition.evaluate(node);
		} catch (BusinessRuleException be) {
			logger.log(Level.SEVERE, "Error occurred while evaluating business condition with ID '" + condition.getID() + "' for node with ID '" + node.getID() + "'", be);
			throw new BusinessRulePluginException(be);
		}
		return conditionResult.isAccepted();
	}

	private void executeBusinessAction(Node node, Logger logger) throws BusinessRulePluginException {
		try {
			BusinessActionResult actionResult = action.execute(node);
			if (actionResult.isNonApplicable()) {
			    logger.info("Business action with ID '" + action.getID() + "' is not applicable for node with ID '" + node.getID() + "'");
			}
		} catch (BusinessRuleException be) {
			logger.log(Level.SEVERE, "Error occurred while executing business action with ID '" + action.getID() + "' for node with ID '" + node.getID() + "'", be);
			throw new BusinessRulePluginException(be);
		}
	}
}
