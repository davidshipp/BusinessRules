package com.example.businessrule.function.allergenwarning;

import com.stibo.core.domain.Attribute;
import com.stibo.core.domain.Node;
import com.stibo.core.domain.Product;
import com.stibo.core.domain.ValidatorException;
import com.stibo.core.domain.businessrule.plugin.BusinessActionPlugin;
import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.core.domain.businessrule.plugin.function.parameter.BusinessFunctionParameter;
import com.stibo.core.domain.businessrule.plugin.function.proxy.BusinessFunctionProxy;
import com.stibo.framework.Plugin;
import com.stibo.framework.PluginDescription;
import com.stibo.framework.PluginName;
import com.stibo.framework.PluginParameter;
import com.stibo.framework.Required;

/**
 * This example shows a business action implementation that uses a business function with an "interface" similar to
 * {@link AllergenWarningProducer} for generating a value to be written to an attribute.
 * <p>
 * Notice how the "interface" for valid functions is specified by defining an interface that extends
 * {@link BusinessFunctionProxy}.
 */
@Plugin(id = "com.example.businessrule.function.allergenwarning.SetAllergenWarning")
@PluginName("Set Allergen Warning")
@PluginDescription("Action that sets allergen warning using business function")
public class SetAllergenWarning
		implements BusinessActionPlugin<SetAllergenWarning.Parameters, SetAllergenWarning.Context>{

    private Attribute warningAttribute;
    private AllergenWarningProducerFunction warningProducerFunction;

	public interface Parameters {

		@Required
		@PluginParameter(name = "Allergen Warning Producer Function", description = "A BF that can produce an allergen warning", priority = 100)
		AllergenWarningProducerFunction getFunction();

		@Required
		@PluginParameter(name = "Warning Attribute", description = "The attribute to write the warning to", priority = 200)
		Attribute getWarningAttribute();
	}

	public interface Context {
		Node getNode();
	}

	public interface AllergenWarningProducerFunction extends BusinessFunctionProxy<String> {

		@BusinessFunctionParameter(description = "The recipe product to produce the warning for")
		AllergenWarningProducerFunction setRecipeProduct(Product recipeProduct);
	}

	@Override
	public void executeAction(Context context) throws BusinessRulePluginException {
		Node recipeObject = context.getNode();
		if (!(recipeObject instanceof Product)) {
			throw new RecipeObjectIncompatibilityException();
		}

		String warningText = warningProducerFunction.setRecipeProduct((Product) recipeObject).evaluate();

		try {
			recipeObject.getValue(warningAttribute).setSimpleValue(warningText);
		} catch (ValidatorException e) {
			throw new BusinessRulePluginException(e);
		}

	}

	@Override
	public Object getDescription() {
		return null;
	}

	@Override
	public void initFromConfiguration(Parameters parameters) {
		warningAttribute = parameters.getWarningAttribute();
		warningProducerFunction = parameters.getFunction();
	}
}
