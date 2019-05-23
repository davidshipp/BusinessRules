package com.example.businessrule.condition.isleaf;

import com.stibo.core.domain.Node;
import com.stibo.core.domain.businessrule.plugin.BusinessConditionPlugin;
import com.stibo.core.domain.businessrule.plugin.BusinessConditionPluginResult;
import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.core.domain.datatreenode.DataTreeNode;
import com.stibo.framework.Plugin;
import com.stibo.framework.PluginName;

/**
 * This examples shows how to create a simple business condition plugin that throws an exception with a localizable
 * message when used for objects of incompatible types and returns a localizable message when evaluating to false.
 * <p>
 * See resources > META-INF.stibo.localization > messages_da.properties for an example localization file.
 */
@Plugin(id = "com.example.businessrule.condition.isleaf.IsLeaf")
@PluginName("Is Leaf")
public class IsLeaf implements BusinessConditionPlugin<IsLeaf.Parameters, IsLeaf.Context> {

    public interface Parameters {}

    public interface Context {
        Node getCurrentObject();
    }

    @Override
    public BusinessConditionPluginResult evaluateCondition(Context context) throws BusinessRulePluginException {

        if (!(context.getCurrentObject() instanceof DataTreeNode)) {
            throw new IncompatibilityException();
        }

        DataTreeNode node = (DataTreeNode) context.getCurrentObject();

        if (node.queryChildren().asList(1).isEmpty()) {
            return new BusinessConditionPluginResult(false, new AcceptanceMessage());
        }

        return new BusinessConditionPluginResult(true, new RejectionMessage(node.getID()));
    }

    @Override
    public Object getDescription() {
        return "Will evaluate to true if the entity / product / classification does not have any children";
    }

    @Override
    public void initFromConfiguration(Parameters parameters) {}

}
