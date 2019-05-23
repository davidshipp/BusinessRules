package com.example.businessrule.function.allergenwarning;

import java.util.List;

import com.stibo.core.domain.Attribute;
import com.stibo.core.domain.LOVSingleValue;
import com.stibo.core.domain.Manager;
import com.stibo.core.domain.Product;
import com.stibo.core.domain.Reference;
import com.stibo.core.domain.ReferenceType;
import com.stibo.core.domain.Value;
import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.core.domain.businessrule.plugin.function.BusinessFunctionPlugin;
import com.stibo.framework.Plugin;
import com.stibo.framework.PluginDescription;
import com.stibo.framework.PluginName;
import com.stibo.framework.PluginParameter;
import com.stibo.framework.Required;

/**
 * This example shows how to create a business function plugin.
 */
@Plugin(id = "com.example.businessrule.function.allergenwarning.AllergenWarningProducer")
@PluginName("Allergen Warning Producer")
@PluginDescription("Function that can produce an allergen warning for a recipe based on ingredients")
public class AllergenWarningProducer
		implements BusinessFunctionPlugin<AllergenWarningProducer.Parameters, AllergenWarningProducer.Context, String> {

	private Product recipeProduct;
	private ReferenceType ingredientRefType;
	private List<Attribute> allergenAttributes;

	public interface Parameters {

		@Required
		@PluginParameter(name = "Recipe Product", description = "The recipe product object referencing ingredients", priority = 100)
		Product getRecipeProduct();

		@Required
		@PluginParameter(name = "Reference Type", description = "The type used for recipe to ingredient references", priority = 200)
		ReferenceType getReferenceType();

		@Required
		@PluginParameter(name = "Allergen Info Attribute", description = "Attributes must be LOV based and use a LOV value with ID \"Y\" to indicate allergens", priority = 300)
		List<Attribute> getAllergenInfoAttributes();
	}

	public interface Context {
		Manager getManager();
	}

	@Override
	public String evaluate(Context context) throws BusinessRulePluginException {
		for (Reference ingredientRef : recipeProduct.getReferences(ingredientRefType)) {
			for (Attribute attribute : allergenAttributes) {
				Value value = ingredientRef.getTarget().getValue(attribute);
				if (value instanceof LOVSingleValue &&
						value.getAttribute().getListOfValues().isUsingValueIDs() &&
						"Y".equals(((LOVSingleValue) value).getID())) {
					return "Contains allergens";
				}
			}
		}

		return "Does not contain allergens";
	}

	@Override
	public Object getDescription() {
		return "This function produces an allergen warning text based on the configured input parameters";
	}

	@Override
	public void initFromConfiguration(final Parameters parameters) {
		recipeProduct = parameters.getRecipeProduct();
		ingredientRefType = parameters.getReferenceType();
		allergenAttributes = parameters.getAllergenInfoAttributes();
	}
}
