package ru.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.scooter.model.Courier;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Авторизация курьера")
    public Response login(Courier courier) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Авторизация курьера без пароля")
    public Response loginWithoutPassword(String login) {
        Map<String, Object> json = new HashMap<>();
        json.put("login", login);
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера")
    public Response delete(int courierId) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}
