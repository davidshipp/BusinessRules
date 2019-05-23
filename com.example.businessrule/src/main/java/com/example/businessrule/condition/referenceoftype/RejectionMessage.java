package com.example.businessrule.condition.referenceoftype;

import com.stibo.framework.localization.Localizable;

@Localizable("No references of type '{referenceTypeId}' found")
public class RejectionMessage {
    private String referenceTypeId;

    public RejectionMessage(String referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }

    public String getReferenceTypeId() {
        return referenceTypeId;
    }
}
