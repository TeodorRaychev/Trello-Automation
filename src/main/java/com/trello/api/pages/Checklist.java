package com.trello.api.pages;

import com.google.gson.JsonObject;
import io.restassured.response.Response;

import java.text.MessageFormat;

public class Checklist extends BasePage {
    public Response createChecklist(String checklistName, String cardId) {
        JsonObject authenticatedJsonObject = getAuthenticatedJsonObject();
        authenticatedJsonObject.addProperty("idCard", cardId);
        authenticatedJsonObject.addProperty("name", checklistName);
        Response response = post(checklistsUrl, authenticatedJsonObject);
        assert response.statusCode() == 200 : "Failed to create checklist";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        assert id != null : "Checklist ID is null";
        String name = response.body().jsonPath().getString("name");
        assert name != null : "Checklist name is null";
        logger.info(MessageFormat.format("Successfully created checklist [{0}] with Checklist ID: [{1}]", name, id));
        return response;
    }

    public Response addCheckItemsToChecklist(String checkItemName, String checklistId, boolean completed) {
        JsonObject authenticatedJsonObject = getAuthenticatedJsonObject();
        authenticatedJsonObject.addProperty("name", checkItemName);
        authenticatedJsonObject.addProperty("checked", completed);
        Response response = post(String.format("%s/%s/checkItems", checklistsUrl, checklistId), authenticatedJsonObject);
        assert response.statusCode() == 200 : "Failed to add check item to checklist";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        assert id != null : "Checklist ID is null";
        String name = response.body().jsonPath().getString("name");
        assert name != null : "Checklist name is null";
        logger.info(MessageFormat.format("Successfully added check item [{0}] with Check Item ID: [{1}]", name, id));
        return response;
    }

    public Response getChecklist(String checklistId) {
        Response response = get(String.format("%s/%s", checklistsUrl, checklistId), authenticatedQueryParams);
        assert response.statusCode() == 200 : "Failed to get checklist";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        assert id != null : "Checklist ID is null";
        String name = response.body().jsonPath().getString("name");
        assert name != null : "Checklist name is null";
        logger.info(MessageFormat.format("Successfully retrieved checklist [{0}] with Checklist ID: " +
                "[{1}]", name, id));
        return response;
    }

    public static String formatCheckItemName(String checklistName, int checkItemNumber) {
        return String.format("%s Check Item %d", checklistName, checkItemNumber);
    }
}
