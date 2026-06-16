## veracode-get-expiring-grace-periods-java

This example project builds a simple Java CLI application which fetches all expiring mitigations in the next 1, 3, and 7 days. 
By default, it only prits the data to the CLI, but it the GetExpiringGracePeriods.class file can be modified to use the returned information and, modify numbers of days to warn on (or even add more groups).

## Possible changes:
- The DAYS_IN_ADVANCE_TO_WARN constant contains a list of days in advance to warn, can be modified to use different time periods or even increase the number of date intervals
- The useFindingInformation function has access to each finding an its GracePeriodWindow. It can be used to send notifications or use the finding/grace period information
