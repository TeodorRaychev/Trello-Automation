package com.trello.api;

import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.MessageFormat;

import static com.trello.api.enums.Groups.CREATE_LIST;
import static com.trello.api.enums.Status.DONE;
import static com.trello.api.enums.Status.IN_PROGRESS;
import static com.trello.api.enums.Status.TODO;

public class BoardSetupTest extends BaseTest {

    @Test
    public void test_createBoard() {
        final String boardName = "Interview Board";
        Response response = board.createBoard(boardName);
        boardId = response.body().jsonPath().getString("id");
    }

    @Test(dataProvider = "listNames", dependsOnMethods = "test_createBoard", groups = {CREATE_LIST})
    public void test_createList(String listName) {
        assert boardId != null : "Board ID is null";
        Response response = board.createList(boardId, listName);
        String id = response.body().jsonPath().getString("id");
        String listBoardId = response.body().jsonPath().getString("idBoard");
        assert listBoardId != null : "List board ID is null";
        assert listBoardId.equals(boardId) : MessageFormat.format("List board ID: [{0}] " +
                "does not match expected board ID: [{1}]", listBoardId, boardId);
        listMap.put(listName, id);
    }

    @Test
    @DataProvider
    Object[][] listNames() {
        return new Object[][]{
                {TODO.getStatus()},
                {IN_PROGRESS.getStatus()},
                {DONE.getStatus()}
        };
    }
}
