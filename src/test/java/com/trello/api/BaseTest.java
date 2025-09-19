package com.trello.api;

import com.trello.api.pages.Board;
import org.testng.annotations.AfterSuite;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    static final Board board = new Board();
    static String boardId;
    static String cardId;
    static Map<String, String> listMap = new HashMap<>();

    @AfterSuite
    public static void deleteBoard() {
        assert boardId != null : "Board ID is null";
        board.deleteBoard(boardId);
    }
}
