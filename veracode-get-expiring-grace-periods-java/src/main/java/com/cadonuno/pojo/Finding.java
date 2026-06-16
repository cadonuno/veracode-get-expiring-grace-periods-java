package com.cadonuno.pojo;

public class Finding {
    private final int issueId;
    private final int severity;
    private final String firstFoundDate;

    public Finding(int issueId, int severity, String firstFoundDate) {
        this.issueId = issueId;
        this.severity = severity;
        this.firstFoundDate = firstFoundDate;
    }

    public int getIssueId() {
        return issueId;
    }

    public int getSeverity() {
        return severity;
    }

    public String getFirstFoundDate() {
        return firstFoundDate;
    }
}
