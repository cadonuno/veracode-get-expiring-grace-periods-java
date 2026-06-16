package com.cadonuno.pojo;

import com.cadonuno.GetExpiringGracePeriods;

import java.util.ArrayList;
import java.util.List;

public class Policy {
    private final String guid;
    private final Integer sev5GracePeriod;
    private final Integer sev4GracePeriod;
    private final Integer sev3GracePeriod;
    private final Integer sev2GracePeriod;
    private final Integer sev1GracePeriod;
    private final Integer sev0GracePeriod;

    public Policy(String guid) {
        this(guid, null, null, null, null, null, null);
    }

    public Policy(String guid,
                  Integer sev5GracePeriod,
                  Integer sev4GracePeriod,
                  Integer sev3GracePeriod,
                  Integer sev2GracePeriod,
                  Integer sev1GracePeriod,
                  Integer sev0GracePeriod) {
        this.guid = guid;
        this.sev5GracePeriod = sev5GracePeriod;
        this.sev4GracePeriod = sev4GracePeriod;
        this.sev3GracePeriod = sev3GracePeriod;
        this.sev2GracePeriod = sev2GracePeriod;
        this.sev1GracePeriod = sev1GracePeriod;
        this.sev0GracePeriod = sev0GracePeriod;
    }

    public String getGuid() {
        return guid;
    }

    public Integer getSev5GracePeriod() {
        return sev5GracePeriod;
    }

    public Integer getSev4GracePeriod() {
        return sev4GracePeriod;
    }

    public Integer getSev3GracePeriod() {
        return sev3GracePeriod;
    }

    public Integer getSev2GracePeriod() {
        return sev2GracePeriod;
    }

    public Integer getSev1GracePeriod() {
        return sev1GracePeriod;
    }

    public Integer getSev0GracePeriod() {
        return sev0GracePeriod;
    }
}
