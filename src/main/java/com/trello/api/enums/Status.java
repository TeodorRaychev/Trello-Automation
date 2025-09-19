package com.trello.api.enums;

public enum Status {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");
    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
