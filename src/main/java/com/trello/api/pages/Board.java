package com.trello.api.pages;

import com.google.gson.JsonObject;
import io.restassured.response.Response;

import java.text.MessageFormat;

public class Board extends BasePage {
    public Response createBoard(String name) {
        JsonObject jsonObject = getAuthenticatedJsonObject();
        jsonObject.addProperty("name", name);
        Response response = post(boardsUrl, jsonObject);
        assert response.statusCode() == 200 : "Failed to create board";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        logger.info(MessageFormat.format("Successfully created board [{0}] with Board ID: [{1}]", name, id));
        return response;
    }

    public void deleteBoard(String id) {
        JsonObject jsonObject = getAuthenticatedJsonObject();
        Response response = delete(boardsUrl + id, jsonObject);
        assert response.statusCode() == 200 : "Failed to delete board";
        logger.info(MessageFormat.format("Successfully deleted board  with Board ID: [{0}]", id));
    }

    public Response createList(String boardId, String name) {
        JsonObject jsonObject = getAuthenticatedJsonObject();
        jsonObject.addProperty("name", name);
        Response response = post(boardsUrl + boardId + "/lists", jsonObject);
        assert response.statusCode() == 200 : "Failed to create list";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        logger.info(MessageFormat.format("Successfully created list [{0}] with List ID: [{1}]", name, id));
        return response;
    }
}
