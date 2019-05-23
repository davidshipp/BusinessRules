package com.example.businessrule.condition.isleaf;

import com.stibo.framework.localization.Localizable;

@Localizable("Object with ID '{id}' is not a leaf")
public class RejectionMessage {
    private String id;

    public RejectionMessage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
