package com.cadonuno.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppInfo {
    private static Map<String, PolicyInfo> policyInfoCache = new HashMap<>();
    private final String guid;
    private final String appName;
    private final String ownerName;
    private final String ownerEmail;
    private final Policy policy;

    public AppInfo(String guid, String appName, String ownerName, String ownerEmail, Policy policy) {
        this.guid = guid;
        this.appName = appName;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.policy = policy;
    }

    public String getGuid() {
        return guid;
    }

    public String getAppName() {
        return appName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public PolicyInfo getPolicyInfo() {
        return policyInfoCache.computeIfAbsent(policy.getGuid(), pol -> PolicyInfo.from(policy));
    }
}