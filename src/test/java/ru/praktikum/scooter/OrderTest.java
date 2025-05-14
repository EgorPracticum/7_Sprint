package ru.praktikum.scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.scooter.client.OrderClient;
import ru.praktikum.scooter.model.Order;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private final OrderClient orderClient = new OrderClient();
    private final List<String> color;
    private final String testName;

    public OrderTest(String testName, List<String> color) {
        this.testName = testName;
        this.color = color;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getColorData() {
        return new Object[][] {
                {"Черный самокат", Arrays.asList("BLACK")},
                {"Серый самокат", Arrays.asList("GREY")},
                {"Два цвета", Arrays.asList("BLACK", "GREY")},
                {"Без указания цвета", null}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void testCreateOrderWithDifferentColors() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "Москва, ул. Ленина, 1",
                "Черкизовская",
                "89152002020",
                1,
                "2025-05-11",
                "Позвоните за час",
                color
        );

        Response response = orderClient.create(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        Integer track = response.then().extract().path("track");
        assertTrue(track > 0);
    }
}