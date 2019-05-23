package com.example.businessrule.condition.isleaf;

import com.stibo.core.domain.businessrule.plugin.BusinessRulePluginException;
import com.stibo.framework.localization.Localizable;

@Localizable("Plugin only works for entities, products and classifications")
public class IncompatibilityException extends BusinessRulePluginException {

}
