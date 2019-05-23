package com.example.businessrule.action.triggerevent;

import com.stibo.core.domain.Node;
import com.stibo.core.domain.WorkflowableNode;
import com.stibo.core.domain.businessrule.plugin.BusinessActionPlugin;
import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.core.domain.state.Task;
import com.stibo.core.domain.state.Workflow;
import com.stibo.framework.Plugin;
import com.stibo.framework.PluginName;
import com.stibo.framework.PluginParameter;
import com.stibo.framework.Required;

/**
 * This example shows a simple business action plugin that triggers a configured event in "current workflow" for
 * "current object" in specified state.
 */
@Plugin(id = "com.example.businessrule.action.triggerevent.TriggerEventInCurrentWorkflow")
@PluginName("Trigger Event in Current Workflow")
public class TriggerEventInCurrentWorkflow implements BusinessActionPlugin<TriggerEventInCurrentWorkflow.Parameters, TriggerEventInCurrentWorkflow.Context>{

    private String eventID;
    private String stateID;

    public interface Parameters {

        @Required
        @PluginParameter(name = "Event ID", description = "ID of event to trigger")
        String getEventID();

        @Required
        @PluginParameter(name = "State ID", description = "ID of the state that the object must be in when event is triggered")
        String getStateID();
    }

    public interface Context {
        Workflow getCurrentWorkflow();
        Node getCurrentObject();
    }

    @Override
    public void executeAction(Context context) throws BusinessRulePluginException {

        Workflow workflow = context.getCurrentWorkflow();

        if (workflow != null) {
            WorkflowableNode node = (WorkflowableNode) context.getCurrentObject();
            Task task = node.getWorkflowInstance(workflow).getTaskByID(stateID);
            if (task == null) {
                throw new IncorrectTaskWorkflowStateException(stateID);
            }

            task.triggerLaterByID(eventID, "Transition triggered from business action");
        }
    }

    @Override
    public Object getDescription() {
        return null;
    }

    @Override
    public void initFromConfiguration(Parameters parameters) {
        eventID = parameters.getEventID();
        stateID = parameters.getStateID();
    }
}
