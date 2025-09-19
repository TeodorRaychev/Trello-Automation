package com.trello.api;

import com.trello.api.pages.Card;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.MessageFormat;

import static com.trello.api.enums.Groups.CREATE_CARD;
import static com.trello.api.enums.Groups.CREATE_LIST;
import static com.trello.api.enums.Status.DONE;
import static com.trello.api.enums.Status.IN_PROGRESS;
import static com.trello.api.enums.Status.TODO;
import static com.trello.api.enums.Status.values;

public class CardWorkflowTest extends BaseTest {
    Card card = new Card();
    private final String comment = "Prepare Interview Task Card Comment";

    @Test(dependsOnGroups = {CREATE_LIST})
    public void test_createCard() {
        final String cardName = "Prepare Interview Task";
        assert boardId != null : "Board ID is null";
        assert listMap.size() == values().length : "List Map size does not match Status values";
        String listId = listMap.get(TODO.getStatus());
        assert listId != null : "List ID is null";
        Response response = card.createCard(cardName, listId);
        String cardListId = response.body().jsonPath().getString("idList");
        assert cardListId != null : "Card list ID is null";
        assert cardListId.equals(listId) : MessageFormat.format("Card list ID: [{0}] " +
                "does not match expected list ID: [{1}]", cardListId, listId);
        String id = response.body().jsonPath().getString("id");
        assert id != null : "Card ID is null";
        cardId = id;
    }

    @Test(dependsOnMethods = "test_createCard", dataProvider = "moveListNames")
    public void test_moveCard(String listName) {
        assert cardId != null : "Card ID is null";
        String listId = listMap.get(listName);
        assert listId != null : "List ID is null";
        card.moveCard(cardId, listId);
        String cardListId = card.getCardLocation(cardId);
        assert cardListId != null : "Card list ID is null";
        assert cardListId.equals(listId) : MessageFormat.format("Card list ID: [{0}] " +
                "does not match expected list ID: [{1}]", cardListId, listId);
    }

    @Test(dependsOnMethods = "test_moveCard")
    public void test_addCardComment() {
        assert cardId != null : "Card ID is null";
        Response response = card.addCardComment(cardId, comment);
        assert response.body().jsonPath().getString("data.text").equals(comment) : "Card comment " +
                "text does not match";
    }

    @Test(dependsOnMethods = "test_addCardComment", groups = {CREATE_CARD})
    public void test_getCardActions() {
        assert cardId != null : "Card ID is null";
        Response response = card.getCardActions(cardId);
        String cardType = response.body().jsonPath().getString("[0].type");
        assert cardType.equals("commentCard") : MessageFormat.format("Card action type: [{0}]" +
                "does not match expected action type: [{1}]", cardType, "commentCard");
        String commentText = response.body().jsonPath().getString("[0].data.text");
        assert commentText != null : "Card comment text is null";
        assert commentText.equals(comment) : MessageFormat.format("Card comment text: [{0}] does not match expected " +
                "comment text: [{1}]", commentText, comment);
    }


    @DataProvider
    public Object[][] moveListNames() {
        return new Object[][]{
                {IN_PROGRESS.getStatus()},
                {DONE.getStatus()}
        };
    }
}
