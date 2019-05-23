package com.example.businessrule.function.productcategory;

import com.stibo.core.domain.ObjectType;
import com.stibo.core.domain.Product;
import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.core.domain.businessrule.plugin.function.BusinessFunctionPlugin;
import com.stibo.framework.*;

/**
 * Example showing a simple business function plugin implementation.
 * <p>
 * The plugin produces a "category" product object of a specified object type for a given product or null if the product
 * is not in a category.
 * <p>
 * The category object type parameter could be configured in the UI (fixed value). Notice that the implementation makes
 * no assumptions regarding how parameters are provided.
 */
@Plugin(id = "com.example.businessrule.function.productcategory.ProductCategory")
@PluginName("Get Product Category")
@PluginDescription("Function that traverses the hierarchy upwards returning the first product of the supplied category object type and null if none is found")
public class ProductCategory implements BusinessFunctionPlugin<ProductCategory.Parameters, ProductCategory.Context, Product>{

    private ObjectType categoryObjectType;
    private Product product;

    public interface Parameters {

        @Required
        @PluginParameter(name = "Category object type", description = "Product object type used for categories", priority = 100)
        ObjectType getCategoryType();

        @Required
        @PluginParameter(name = "Product", description = "The product to return the category for", priority = 200)
        Product getProduct();
    }

    public interface Context {}

    @Override
    public Product evaluate(Context context) throws BusinessRulePluginException {

        Product currentProduct = product;
        while (currentProduct.getParent() != null) {
            Product parent = currentProduct.getParent();
            if (parent.getObjectType().equals(categoryObjectType)) {
                return parent;
            }
            currentProduct = parent;
        }
        return null;
    }

    @Override
    public Object getDescription() {
        return "Function that traverses the hierarchy upwards returning the first product of the supplied category object type and null if none is found";
    }

    @Override
    public void initFromConfiguration(Parameters parameters) {
        categoryObjectType = parameters.getCategoryType();
        product = parameters.getProduct();
    }


}
