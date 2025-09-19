package com.trello.api.pages;

import com.google.gson.JsonObject;
import io.restassured.response.Response;

import java.text.MessageFormat;

public class Card extends BasePage {

    public Response createCard(String name, String listId) {
        JsonObject jsonObject = getAuthenticatedJsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("idList", listId);
        Response response = post(cardsUrl, jsonObject);
        assert response.statusCode() == 200 : "Failed to create card";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        logger.info(MessageFormat.format("Successfully created card [{0}] with Card ID: [{1}]", name, id));
        return response;
    }

    public Response moveCard(String cardId, String listId) {
        JsonObject jsonObject = getAuthenticatedJsonObject();
        jsonObject.addProperty("idList", listId);
        Response response = put(cardsUrl + cardId, jsonObject);
        assert response.statusCode() == 200 : "Failed to move card";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        assert id != null : "Card ID is null";
        String name = response.body().jsonPath().getString("name");
        assert name != null : "Card name is null";
        logger.info(MessageFormat.format("Successfully moved card [{0}] with Card ID: [{1}]", name, id));
        return response;
    }

    public Response getCard(String cardId) {
        Response response = get(cardsUrl + cardId, authenticatedQueryParams);
        assert response.statusCode() == 200 : "Failed to get card";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        String name = response.body().jsonPath().getString("name");
        logger.info(MessageFormat.format("Successfully retrieved card [{0}] with Card ID: [{1}]", name, id));
        return response;
    }

    public String getCardLocation(String cardId) {
        Response response = getCard(cardId);
        String idList = response.body().jsonPath().getString("idList");
        assert idList != null : "Card idList is null";
        return idList;
    }

    public Response addCardComment(String cardId, String comment) {
        JsonObject jsonObject = getAuthenticatedJsonObject();
        jsonObject.addProperty("text", comment);
        Response response = post(cardsUrl + cardId + "/actions/comments", jsonObject);
        assert response.statusCode() == 200 : "Failed to add card comment";
        logger.fine(response.body().asString());
        String id = response.body().jsonPath().getString("id");
        assert id != null : "Card comment ID is null";
        String text = response.body().jsonPath().getString("data.text");
        assert text != null : "Card comment text is null";
        logger.info(MessageFormat.format("Successfully added card comment [{0}] with Card Comment ID: [{1}]", text, id));
        return response;
    }

    public Response getCardActions(String cardId) {
        Response response = get(cardsUrl + cardId + "/actions", authenticatedQueryParams);
        assert response.statusCode() == 200 : "Failed to get card actions";
        logger.fine(response.body().asString());
        return response;
    }
}
