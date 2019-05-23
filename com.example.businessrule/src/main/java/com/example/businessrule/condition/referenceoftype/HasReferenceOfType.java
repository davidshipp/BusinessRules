package com.example.businessrule.condition.referenceoftype;

import com.stibo.core.domain.Node;
import com.stibo.core.domain.ReferenceList;
import com.stibo.core.domain.ReferenceSource;
import com.stibo.core.domain.ReferenceType;
import com.stibo.core.domain.businessrule.plugin.BusinessConditionPlugin;
import com.stibo.core.domain.businessrule.plugin.BusinessConditionPluginResult;
import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.framework.Plugin;
import com.stibo.framework.PluginName;
import com.stibo.framework.PluginParameter;
import com.stibo.framework.Required;

/**
 * This examples shows how to create a simple business condition plugin that tests whether or not "current object" has
 * one or more references of a reference type selected by the user configuring the plugin.
 * <p>
 * The plugin returns a localizable message when evaluating to false. See resources > META-INF.stibo.localization >
 * messages_da.properties for an example localization file.
 */
@Plugin(id = "com.example.businessrule.condition.referenceoftype.HasReferenceOfType")
@PluginName("Has Reference of Type")
public class HasReferenceOfType implements BusinessConditionPlugin<HasReferenceOfType.Parameters, HasReferenceOfType.Context> {

    private ReferenceType referenceType;
    private boolean includeInherited;

    public interface Parameters {

        @Required
        @PluginParameter(name = "Reference type", description = "Does the current object have one or more references of this type", priority = 100)
        ReferenceType getReferenceType();

        @PluginParameter(name = "Include inherited", description = "Should the analysis include inherited references", priority = 200)
        boolean getIncludeInherited();
    }

    public interface Context {
        Node getCurrentObject();
    }

    @Override
    public BusinessConditionPluginResult evaluateCondition(Context context) throws BusinessRulePluginException {
        RejectionMessage rejectionMessage = new RejectionMessage(referenceType.getID());

        if (context.getCurrentObject() instanceof ReferenceSource) {
            ReferenceSource source = (ReferenceSource) context.getCurrentObject();
            ReferenceList references = includeInherited ? source.getReferences(referenceType) : source.getLocalReferences(referenceType);
            if (!references.isEmpty()) {
                return new BusinessConditionPluginResult(false, new AcceptanceMessage());
            }
        }

        return new BusinessConditionPluginResult(true, rejectionMessage);
    }

    @Override
    public Object getDescription() {
        return "This condition evaluates whether current object has at least one outgoing reference of the specified type";
    }

    @Override
    public void initFromConfiguration(Parameters parameters) {
        referenceType = parameters.getReferenceType();
        includeInherited = parameters.getIncludeInherited();
    }

}
