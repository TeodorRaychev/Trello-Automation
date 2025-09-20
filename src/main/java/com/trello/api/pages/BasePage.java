package com.trello.api.pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.trello.util.ConfigLoader.loadConfig;
import static com.trello.util.ConfigLoader.setLogLevel;
import static io.restassured.RestAssured.given;

public class BasePage {
    public static final Logger logger = Logger.getLogger(BasePage.class.getName());
    public static final String filePath = "src/test/resources/config/config.properties";

    Properties properties = loadConfig(filePath);

    String baseUrl;
    String boardsUrl;
    String cardsUrl;
    String checklistsUrl;
    String apiKey;
    String apiToken;
    Map<String, String> authenticatedQueryParams = new HashMap<>();
    JsonObject autheticatedJsonObject;

    public BasePage() {
        assert properties != null : "Properties file not found";
        Level logLevel = Level.parse(properties.getProperty("logLevel").toUpperCase());
        setLogLevel(logger, logLevel);
        baseUrl = properties.getProperty("baseUrl");
        boardsUrl = baseUrl + "boards/";
        cardsUrl = baseUrl + "cards/";
        checklistsUrl = baseUrl + "checklists";
        apiKey = properties.getProperty("APIKey");
        apiToken = properties.getProperty("APIToken");
        autheticatedJsonObject = new JsonObject();
        autheticatedJsonObject.addProperty("key", apiKey);
        autheticatedJsonObject.addProperty("token", apiToken);
        authenticatedQueryParams.put("key", apiKey);
        authenticatedQueryParams.put("token", apiToken);
    }

    public static Response post(String url, JsonObject jsonObject) {
        logger.info(MessageFormat.format("POST: [{0}]", url));
        logger.fine(jsonObject.toString());
        Response response;
        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonObject.toString())
                        .post(url);
        logger.info(MessageFormat.format("Response Code received: [{0}]", response.statusCode()));
        return response;
    }

    public static Response delete(String url, JsonObject jsonObject) {
        logger.info(MessageFormat.format("DELETE: [{0}]", url));
        logger.fine(jsonObject.toString());
        Response response;
        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonObject.toString())
                        .delete(url);
        logger.info(MessageFormat.format("Response Code received: [{0}]", response.statusCode()));
        return response;
    }

    public static Response put(String url, JsonObject jsonObject) {
        logger.info(MessageFormat.format("PUT: [{0}]", url));
        logger.fine(jsonObject.toString());
        Response response;
        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonObject.toString())
                        .put(url);
        logger.info(MessageFormat.format("Response Code received: [{0}]", response.statusCode()));
        return response;
    }

    public static Response get(String url, Map<String, String> queryParams) {
        logger.info(MessageFormat.format("GET: [{0}]", url));
        Response response;
        response =
                given()
                        .contentType(ContentType.JSON)
                        .queryParams(queryParams)
                        .get(url);
        logger.info(MessageFormat.format("Response Code received: [{0}]", response.statusCode()));
        return response;
    }

    JsonObject getAuthenticatedJsonObject() {
        return JsonParser.parseString(autheticatedJsonObject.toString()).getAsJsonObject();
    }
}
