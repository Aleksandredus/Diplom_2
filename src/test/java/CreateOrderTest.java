import User.*;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateOrderTest {
    private ActionSteps actionSteps;
    private User user;
    private Credentials credentials;
    private String accessToken;
    private ValidationUserSteps validationUserSteps;
    private int code;
    private boolean statys;

    @Before
    public void setOrder() {
        user = UserMaker.random();
        actionSteps = new ActionSteps();
        credentials = new Credentials(user);
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Проверка ответа успешного создания заказа с ингридиентами авторизованного пользователя")
    public void createOrder() {
        actionSteps.createNewUser(user);
        ValidatableResponse response = actionSteps.loginUser(credentials);
        accessToken = response.extract().path("accessToken").toString();
        actionSteps.createOrderOfFamousUser(accessToken);
        ValidationUserSteps.createOrderResponse(response, code, statys);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка получения ошибки 401 при созданис заказа неавторезированым пользователем")
    public void createOrderWithoutUserAutorization() {
        ValidatableResponse response = actionSteps.createOrderOfUnknownUser();
        validationUserSteps.createOrderWithoutUser(response);
    }

    @Test
    @DisplayName("Создание заказа с невернымс неверным хешем ингредиентов ")
    @Description("Проверка получения ошибки 400 при создании заказа c неизвестными ингридиентами")
    public void createOrderWithIncorrectIngredient() {
        actionSteps.createNewUser(user);
        ValidatableResponse response = actionSteps.loginUser(credentials);
        accessToken = response.extract().path("accessToken").toString();
        actionSteps.createOrderWithUnknownIngredients(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    @Description("Проверка получения ошибки 400 при создании заказа без ингридиентов")
    public void createOrderWithoutIngredient() {
        actionSteps.createNewUser(user);
        ValidatableResponse response = actionSteps.loginUser(credentials);
        accessToken = response.extract().path("accessToken").toString();
        actionSteps.createWithoutIngredients(accessToken);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            actionSteps.deleteUser(accessToken);
        }
    }

}
