package ru.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.scooter.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response create(Order order) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .when()
                .get(ORDER_PATH);
    }
}
