package ru.praktikum.scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.client.CourierClient;
import ru.praktikum.scooter.model.Courier;

import static org.hamcrest.Matchers.*;

public class CourierTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier("testCourier_" + System.currentTimeMillis(), "password", "Test Name");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourierSuccess() {
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void testCreateDuplicateCourier() {
        courierClient.create(courier);
        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.then().extract().path("id");

        Response response = courierClient.create(courier);

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void testCreateCourierWithoutLogin() {
        courier.setLogin(null);
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        courier.setPassword(null);
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void testCreateCourierWithoutFirstName() {
        courier.setFirstName(null);
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void testLoginCourierSuccess() {
        courierClient.create(courier);

        Response response = courierClient.login(courier);
        courierId = response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    public void testLoginWithWrongPassword() {
        courierClient.create(courier);
        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.then().extract().path("id");

        Response response = courierClient.login(new Courier(courier.getLogin(), "wrongPassword", null));

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неправильным логином")
    public void testLoginWithWrongLogin() {
        Response response = courierClient.login(new Courier("nonExistentLogin", "anyPassword", null));

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void testLoginWithoutPassword() {
        courierClient.create(courier);
        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.then().extract().path("id");

        Response response = courierClient.loginWithoutPassword(courier.getLogin());

        response.then()
                .statusCode(400)
                .body(anything());
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void testLoginWithoutLogin() {
        Response response = courierClient.login(new Courier(null, "anyPassword", null));

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    public void testLoginNonExistentCourier() {
        Response response = courierClient.login(courier);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
