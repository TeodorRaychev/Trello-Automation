package com.trello.api.enums;

public enum ChecklistNames {
    PREPARATION_STEPS("Preparation Steps"),
    REVIEW_STEPS("Review Steps");
    private final String checklistName;

    ChecklistNames(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getChecklistName() {
        return checklistName;
    }
}
