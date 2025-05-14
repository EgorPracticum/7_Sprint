package ru.praktikum.scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum.scooter.client.OrderClient;

import static org.hamcrest.Matchers.*;

public class OrderListTest {
    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrdersList() {
        OrderClient client = new OrderClient();
        Response response = client.getOrders();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0))
                .body("orders[0].id", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @Test
    @DisplayName("Проверка структуры заказа в списке")
    public void testOrderStructureInList() {
        OrderClient client = new OrderClient();
        Response response = client.getOrders();

        response.then()
                .statusCode(200)
                .body("orders[0].firstName", notNullValue())
                .body("orders[0].lastName", notNullValue())
                .body("orders[0].address", notNullValue())
                .body("orders[0].metroStation", notNullValue())
                .body("orders[0].phone", notNullValue())
                .body("orders[0].rentTime", notNullValue())
                .body("orders[0].deliveryDate", notNullValue())
                .body("orders[0].track", notNullValue());
    }
}