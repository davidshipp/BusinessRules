package com.example.businessrule.function.allergenwarning;

import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.framework.localization.Localizable;

@Localizable("This action only works for product objects")
public class RecipeObjectIncompatibilityException extends BusinessRulePluginException {
}
