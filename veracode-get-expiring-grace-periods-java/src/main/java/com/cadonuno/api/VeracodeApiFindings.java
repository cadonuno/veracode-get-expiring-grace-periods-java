package com.cadonuno.api;

import com.cadonuno.pojo.Finding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VeracodeApiFindings {
    private static final String BASE_URL = "appsec/v2/applications/";
    private static final String GET_REQUEST = "GET";

    public static List<Finding> getFindings(String appGuid, int severityGte) {
        boolean hasMorePages = true;
        List<Finding> fullList = new ArrayList<>();
        int page = 0;
        while (hasMorePages) {
            List<Finding> pageContents = ApiCaller.runApi(BASE_URL + appGuid + "/findings?severity_gte=" + severityGte + "&page=" + page,
                            GET_REQUEST, null)
                    .flatMap(JsonHandler::getFindingsFromJson)
                    .orElse(Collections.emptyList());
            hasMorePages = !pageContents.isEmpty();
            fullList.addAll(pageContents);
            page++;
        }
        return fullList;
    }
}
