package com.trello.api;

import com.trello.api.pages.Checklist;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static com.trello.api.enums.ChecklistNames.PREPARATION_STEPS;
import static com.trello.api.enums.ChecklistNames.REVIEW_STEPS;
import static com.trello.api.enums.Groups.CREATE_CARD;
import static com.trello.api.pages.BasePage.logger;
import static com.trello.api.pages.Checklist.formatCheckItemName;

public class ChecklistTest extends BaseTest {
    static final Checklist checklist = new Checklist();
    Map<String, String> checklistMap = new HashMap<>();

    @DataProvider
    public static Object[][] checklistNames() {
        return new Object[][]{
                {PREPARATION_STEPS.getChecklistName()},
                {REVIEW_STEPS.getChecklistName()}
        };
    }

    @Test(dependsOnGroups = {CREATE_CARD}, dataProvider = "checklistNames")
    public void test_createChecklist(String checklistName) {
        assert cardId != null : "Card ID is null";
        Response response = checklist.createChecklist(checklistName, cardId);
        String id = response.body().jsonPath().getString("id");
        String name = response.body().jsonPath().getString("name");
        assert name.equals(checklistName) : MessageFormat.format("Checklist name: [{0}] does not match expected " +
                "checklist name: [{1}]", name, checklistName);
        checklistMap.put(checklistName, id);
    }

    @Test(dependsOnMethods = "test_createChecklist", dataProvider = "checklistNames")
    public void test_addCheckItemsToChecklist(String checklistName) {
        String checklistId = checklistMap.get(checklistName);
        assert checklistId != null : "Checklist ID is null";
        int maxCheckItems = checklistName.equals(REVIEW_STEPS.getChecklistName()) ? 3 : 2;
        for (int i = 0; i < maxCheckItems; i++) {
            String checkItemName = formatCheckItemName(checklistName, i + 1);
            boolean checkItemCompleted = !checklistName.equals(REVIEW_STEPS.getChecklistName()) || (i != maxCheckItems - 1);
            Response response =
                    checklist.addCheckItemsToChecklist(checkItemName, checklistId, checkItemCompleted);
            String name = response.body().jsonPath().getString("name");
            assert name.equals(checkItemName) : MessageFormat.format("Check item name: [{0}] " +
                    "does not match expected check item name: [{1}]", name, checkItemName);
        }
    }

    @Test(dependsOnMethods = "test_addCheckItemsToChecklist", dataProvider = "checklistNames")
    public void test_verifyCheckItemsOnChecklist(String checklistName) {
        String checklistId = checklistMap.get(checklistName);
        assert checklistId != null : "Checklist ID is null";
        Response response = checklist.getChecklist(checklistId);
        String name = response.body().jsonPath().getString("name");
        assert name.equals(checklistName) : MessageFormat.format("Checklist name: [{0}] does not match expected " +
                "checklist name: [{1}]", name, checklistName);
        int maxCheckItems = checklistName.equals(REVIEW_STEPS.getChecklistName()) ? 3 : 2;
        for (int i = 0; i < maxCheckItems; i++) {
            String state = response.body().jsonPath().getString(String.format("checkItems[%d].state", i));
            String expectedState = i == 2 ? "incomplete" : "complete";
            assert state.equals(expectedState) : MessageFormat.format("Check item state: [{0}] does not match expected " +
                    "check item state: [{1}]", state, expectedState);
            String checkItemName = response.body().jsonPath().getString(String.format("checkItems[%d].name", i));
            String expectedCheckItemName = formatCheckItemName(checklistName, i +1);
            assert checkItemName.equals(expectedCheckItemName) : MessageFormat.format("Check item name: [{0}] " +
                    "does not match expected check item name: [{1}]", checkItemName, expectedCheckItemName);
            logger.info(MessageFormat.format("Successfully verified check item [{0}] with status:" +
                    " [{1}] for checklist [{2}]", checkItemName, state, checklistName));
        }
    }
}
