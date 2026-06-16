package com.cadonuno.api;
import com.cadonuno.pojo.AppInfo;
import com.cadonuno.pojo.Finding;
import com.cadonuno.pojo.Policy;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JsonHandler {
    public static Optional<List<AppInfo>> getApplicationFromJson(JSONObject apiCallResult) {
        return Optional.of(apiCallResult)
                .flatMap(JsonHandler::getEmbeddedNode)
                .flatMap(jsonObject -> getJsonArrayNode("applications", jsonObject))
                .map(jsonArray -> parseArrayAsObject(jsonArray, JsonHandler::buildAppInfo));
    }

    public static Optional<List<Finding>> getFindingsFromJson(JSONObject apiCallResult) {
        return Optional.of(apiCallResult)
                .flatMap(JsonHandler::getEmbeddedNode)
                .flatMap(jsonObject -> getJsonArrayNode("findings", jsonObject))
                .map(jsonArray -> parseArrayAsObject(jsonArray, JsonHandler::buildFinding));
    }

    public static Optional<Policy> getPoliciesFromJson(JSONObject apiCallResult) {
            return Optional.of(buildPolicy(apiCallResult));
    }

    private static <T>  List<T>parseArrayAsObject(JSONArray jsonArray, Function<JSONObject, T> processingFunction) {
        List<T> elements = new ArrayList<>();
        for (int currentIndex = 0; currentIndex < jsonArray.length(); currentIndex++) {
            tryGetElementAtJsonArrayIndex(jsonArray, currentIndex)
                    .map(processingFunction)
                    .ifPresent(elements::add);
        }
        return elements;
    }

    private static Policy buildPolicy(JSONObject jsonNode) {
        return new Policy(
                tryGetElementAsString(jsonNode, "guid").orElse(""),
                tryGetElementAsInteger(jsonNode, "sev5_grace_period").orElse(0),
                tryGetElementAsInteger(jsonNode, "sev4_grace_period").orElse(0),
                tryGetElementAsInteger(jsonNode, "sev3_grace_period").orElse(0),
                tryGetElementAsInteger(jsonNode, "sev2_grace_period").orElse(0),
                tryGetElementAsInteger(jsonNode, "sev1_grace_period").orElse(0),
                tryGetElementAsInteger(jsonNode, "sev0_grace_period").orElse(0));
    }

    private static Finding buildFinding(JSONObject jsonNode) {
        Optional<JSONObject> findingDetails = tryGetElementFromJsonObject(jsonNode, "finding_details")
                .filter(result -> result instanceof JSONObject)
                .map(JsonHandler::mapToJsonObject);

        Optional<JSONObject> findingStatus = tryGetElementFromJsonObject(jsonNode, "finding_status")
                .filter(result -> result instanceof JSONObject)
                .map(JsonHandler::mapToJsonObject);

        return new Finding(tryGetElementAsInteger(jsonNode, "issue_id").orElse(0),
                        findingDetails.flatMap(node -> tryGetElementAsInteger(node, "severity")).orElse(0),
                        findingStatus.flatMap(node -> tryGetElementAsString(node, "first_found_date")).orElse(""));
    }

    private static AppInfo buildAppInfo(JSONObject jsonNode) {
        Optional<JSONObject> profileNode = tryGetElementFromJsonObject(jsonNode, "profile")
                .filter(result -> result instanceof JSONObject)
                .map(JsonHandler::mapToJsonObject);
        Optional<JSONObject> businessOwner =
                profileNode.flatMap(node -> tryGetElementFromJsonObject(node, "business_owners")
                .filter(result -> result instanceof JSONArray)
                .map(JsonHandler::mapToJsonArray)
                .flatMap(jsonArray -> tryGetElementAtJsonArrayIndex(jsonArray, 0)));
        Optional<JSONObject> policy =
                profileNode
                        .flatMap(node -> getJsonArrayNode("policies", node))
                        .flatMap(array -> tryGetElementAtJsonArrayIndex(array, 0));
        return new AppInfo(
                tryGetElementAsString(jsonNode, "guid").orElse(""),
                profileNode.flatMap(node -> tryGetElementAsString(node, "name")).orElse(""),
                businessOwner.flatMap(node -> tryGetElementAsString(node, "name")).orElse(""),
                businessOwner.flatMap(node -> tryGetElementAsString(node, "email")).orElse(""),
                policy.flatMap(node -> tryGetElementAsString(node, "guid").map(Policy::new)).orElse(null));
    }

    private static Optional<JSONArray> getJsonArrayNode(String nodeName, JSONObject jsonObject) {
        return tryGetElementFromJsonObject(jsonObject, nodeName)
                .filter(result -> result instanceof JSONArray)
                .map(JsonHandler::mapToJsonArray);
    }

    private static Optional<JSONObject> getEmbeddedNode(JSONObject baseNode) {
        return tryGetElementFromJsonObject(baseNode, "_embedded")
                .filter(result -> result instanceof JSONObject)
                .map(JsonHandler::mapToJsonObject);
    }

    private static String commaDelimitArray(JSONArray jsonArray) {
        StringBuilder arrayAsString = new StringBuilder();
        for (int currentIndex = 0; currentIndex < jsonArray.length(); currentIndex++) {
            if (currentIndex > 0) {
                arrayAsString.append(", ");
            }
            try {
                arrayAsString.append(jsonArray.get(currentIndex));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return arrayAsString.toString();
    }

    private static Optional<String> tryGetElementAsString(JSONObject jsonObject, String elementToGet) {
        return tryGetElementFromJsonObject(jsonObject, elementToGet)
                .filter(result -> result instanceof String)
                .map(result -> (String) result);
    }

    private static Optional<Integer> tryGetElementAsInteger(JSONObject jsonObject, String elementToGet) {
        return tryGetElementFromJsonObject(jsonObject, elementToGet)
                .filter(result -> result instanceof Integer)
                .map(result -> (Integer) result);
    }

    private static Optional<String> tryGetNonStringElementAsString(JSONObject jsonObject, String elementToGet) {
        return tryGetElementFromJsonObject(jsonObject, elementToGet)
                .map(Object::toString);
    }

    private static Optional<JSONObject> tryGetElementAtJsonArrayIndex(JSONArray jsonArray, int indexToGet) {
        try {
            if (jsonArray.length() <= indexToGet) {
                return Optional.empty();
            }
            Object element = jsonArray.get(indexToGet);
            if (element instanceof JSONObject) {
                return Optional.of((JSONObject) element);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static Optional<Object> tryGetElementFromJsonObject(JSONObject jsonObject, String elementToGet) {
        try {
            return Optional.of(jsonObject.get(elementToGet));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    private static JSONObject mapToJsonObject(Object jsonResult) {
        return (JSONObject) jsonResult;
    }

    private static JSONArray mapToJsonArray(Object jsonResult) {
        return (JSONArray) jsonResult;
    }

}
