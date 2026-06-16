package com.cadonuno.pojo;

public class FindingRule {
    private final String type;
    private final String scanType;
    private final int value;

    public FindingRule(String type, String scanType, int value) {
        this.type = type;
        this.scanType = scanType;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getScanType() {
        return scanType;
    }

    public int getValue() {
        return value;
    }
}
