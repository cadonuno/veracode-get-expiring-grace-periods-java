package com.cadonuno.pojo;

import com.cadonuno.api.VeracodeApiPolicies;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cadonuno.GetExpiringGracePeriods.DAYS_IN_ADVANCE_TO_WARN;

public class PolicyInfo {
    private static final Duration HOURS_AROUND_DATE = Duration.ofHours(20);
    private final Map<Integer, Map<Integer, GracePeriodWindow>> gracePeriods = new HashMap<>();

    public static PolicyInfo from(Policy policy) {
        Policy parsedPolicy = VeracodeApiPolicies.getPolicy(policy.getGuid()).orElse(null);
        if (parsedPolicy == null) {
            return null;
        }
        PolicyInfo policyInfo = new PolicyInfo();
        policyInfo.putGracePeriod(5, buildGracePeriod(parsedPolicy.getSev5GracePeriod()));
        policyInfo.putGracePeriod(4, buildGracePeriod(parsedPolicy.getSev4GracePeriod()));
        policyInfo.putGracePeriod(3, buildGracePeriod(parsedPolicy.getSev3GracePeriod()));
        policyInfo.putGracePeriod(2, buildGracePeriod(parsedPolicy.getSev2GracePeriod()));
        policyInfo.putGracePeriod(1, buildGracePeriod(parsedPolicy.getSev1GracePeriod()));
        policyInfo.putGracePeriod(0, buildGracePeriod(parsedPolicy.getSev0GracePeriod()));

        return policyInfo;
    }

    public Map<Integer, Map<Integer, GracePeriodWindow>> getGracePeriods() {
        return gracePeriods;
    }

    public void putGracePeriod(int severity, Map<Integer, GracePeriodWindow> periodMap) {
        gracePeriods.put(severity, periodMap);
    }

    private static Map<Integer, GracePeriodWindow> buildGracePeriod(Integer gracePeriodDays) {
        if (gracePeriodDays == null || gracePeriodDays == 0) {
            return null;
        }

        Instant basePeriod = Instant.now().minus(Duration.ofDays(gracePeriodDays));
        Map<Integer, GracePeriodWindow> gracePeriodMap = new HashMap<>();
        for (Integer daysInAdvance : DAYS_IN_ADVANCE_TO_WARN) {
            gracePeriodMap.put(daysInAdvance, new GracePeriodWindow(
                    basePeriod.plus(Duration.ofDays(daysInAdvance)).minus(HOURS_AROUND_DATE),
                    basePeriod.plus(Duration.ofDays(daysInAdvance)).plus(HOURS_AROUND_DATE)
            ));
        }
        return gracePeriodMap;
    }
}
