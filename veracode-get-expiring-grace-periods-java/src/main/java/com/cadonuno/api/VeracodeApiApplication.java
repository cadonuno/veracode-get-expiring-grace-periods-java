package com.cadonuno.api;

import com.cadonuno.pojo.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VeracodeApiApplication {
    private static final String BASE_URL = "appsec/v1/applications";
    private static final String GET_REQUEST = "GET";

    private static List<AppInfo> getApplicationsBase(String filters) {
        boolean hasMorePages = true;
        List<AppInfo> fullList = new ArrayList<>();
        int page = 0;
        while (hasMorePages) {
            List<AppInfo> pageContents = ApiCaller.runApi(BASE_URL + "?page=" + page + filters,
                            GET_REQUEST, null)
                    .flatMap(JsonHandler::getApplicationFromJson)
                    .orElse(Collections.emptyList());
            hasMorePages = !pageContents.isEmpty();
            fullList.addAll(pageContents);
            page++;
        }
        return fullList;
    }


    public static List<AppInfo> getApplicationsByName(String name) {
        return getApplicationsBase("&name=" + name);
    }

    public static List<AppInfo> getAllApplications() {
        return getApplicationsBase("");
    }
}
