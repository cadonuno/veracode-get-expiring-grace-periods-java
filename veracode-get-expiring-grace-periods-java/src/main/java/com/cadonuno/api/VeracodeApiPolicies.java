package com.cadonuno.api;

import com.cadonuno.pojo.Policy;

import java.util.Optional;

public class VeracodeApiPolicies {
    private static final String BASE_URL = "appsec/v1/policies/";
    private static final String GET_REQUEST = "GET";

    public static Optional<Policy> getPolicy(String policyId) {
        return ApiCaller.runApi(BASE_URL + policyId,
                        GET_REQUEST, null)
                .flatMap(JsonHandler::getPoliciesFromJson);
    }
}
