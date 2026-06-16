package com.cadonuno;

import com.cadonuno.api.VeracodeApiApplication;
import com.cadonuno.api.VeracodeApiFindings;
import com.cadonuno.pojo.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetExpiringGracePeriods {
    public static final List<Integer> DAYS_IN_ADVANCE_TO_WARN = List.of(7, 3, 1);

    public static void run() {
        //List<AppInfo> applications = VeracodeApiApplication.getApplicationsByName("Test%20-%20Grace%20Periods%202");
        List<AppInfo> applications = VeracodeApiApplication.getAllApplications();
        for (AppInfo appInfo : applications) {
            PolicyInfo policyInfo = appInfo.getPolicyInfo();
            if (policyInfo == null) {
                continue;
            }
            Map<Integer, List<Finding>> findingsByPeriod = getFindingsWithPeriod(appInfo, policyInfo);

            System.out.println("Application: " + appInfo.getAppName() + " (Owner: " + appInfo.getOwnerName() + " - " + appInfo.getOwnerEmail() + ")");
            for (Map.Entry<Integer, List<Finding>> entry : findingsByPeriod.entrySet()) {
                Integer dayBatch = entry.getKey();
                System.out.println("  - Warning Period: " + dayBatch + " days in advance");
                for (Finding finding : entry.getValue()) {
                    int severity = finding.getSeverity();
                    String firstFoundDate = finding.getFirstFoundDate();
                    Map<Integer, GracePeriodWindow> gracePeriods = policyInfo.getGracePeriods().get(severity);
                    GracePeriodWindow gracePeriod = gracePeriods == null ? null : gracePeriods.get(dayBatch);
                    System.out.println("    - Finding ID: " + finding.getIssueId());
                    System.out.println("      Severity: " + severity);
                    System.out.println("      First Found Date: " + firstFoundDate);
                    System.out.println("      Grace Period: " + gracePeriod);
                }
            }
        }

    }

    private static int getLowestSeverityWithGracePeriod(PolicyInfo policyInfo) {
        for (int severity = 0; severity <= 5; severity++) {
            if (policyInfo.getGracePeriods().get(severity) != null) {
                return severity;
            }
        }
        return 6;
    }

    private static boolean isWithinWarningPeriod(String firstFoundDateStr, GracePeriodWindow gracePeriod) {
        if (gracePeriod == null || gracePeriod.getStartDate() == null || gracePeriod.getEndDate() == null) {
            return false;
        }
        Instant firstFoundDate = Instant.parse(firstFoundDateStr);
        return !firstFoundDate.isBefore(gracePeriod.getStartDate()) && !firstFoundDate.isAfter(gracePeriod.getEndDate());
    }

    private static Map<Integer, List<Finding>> getFindingsWithPeriod(AppInfo app, PolicyInfo policyInfo) {
        Map<Integer, List<Finding>> findingsWithPeriod = new HashMap<>();
        int severityGte = getLowestSeverityWithGracePeriod(policyInfo);
        for (Finding finding : VeracodeApiFindings.getFindings(app.getGuid(), severityGte)) {
            Map<Integer, GracePeriodWindow> severityGracePeriods = policyInfo.getGracePeriods().get(finding.getSeverity());
            if (severityGracePeriods == null) {
                continue;
            }
            for (int potentialDayBatch : DAYS_IN_ADVANCE_TO_WARN) {
                GracePeriodWindow window = severityGracePeriods.get(potentialDayBatch);
                if (isWithinWarningPeriod(finding.getFirstFoundDate(), window)) {
                    findingsWithPeriod.computeIfAbsent(potentialDayBatch, key -> new ArrayList<>()).add(finding);
                    break;
                }
            }
        }
        return findingsWithPeriod;
    }
}
