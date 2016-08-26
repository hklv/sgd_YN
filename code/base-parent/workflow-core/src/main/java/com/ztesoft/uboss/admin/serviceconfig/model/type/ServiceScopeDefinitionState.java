package com.ztesoft.uboss.admin.serviceconfig.model.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZEN 2012-2-1
 */
public enum ServiceScopeDefinitionState {
    VALID("A"), INVALID("X");

    private final String type;

    ServiceScopeDefinitionState(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private static final Map<String, ServiceScopeDefinitionState> cache = new HashMap<String, ServiceScopeDefinitionState>();

    public static ServiceScopeDefinitionState forName(String name) {
        if (cache.size() <= 0) {
            ServiceScopeDefinitionState[] array = ServiceScopeDefinitionState.values();
            for (ServiceScopeDefinitionState e : array) {
                cache.put(e.getType(), e);
            }
        }
        return cache.get(name);
    }

    public static void main(String[] args) {
        System.out.println(ServiceScopeDefinitionState.VALID);
    }

}
