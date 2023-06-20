import User.*;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetOrderTest {
    private User user;
    private ActionSteps actionSteps;
    private ValidationUserSteps validationUserSteps;
    private String accessToken;
    private Credentials credentials;

    @Before
    public void setUser() {
        user = UserMaker.random();
        actionSteps = new ActionSteps();
        validationUserSteps = new ValidationUserSteps();
        credentials = new Credentials(user);
    }

    @Test
    @DisplayName("Получение списка заказов зарегестрированого пользователя")
    @Description("Проверка успешного получения заказа авторизованного пользователя")
    public void getOrderPositiveTest() {
        actionSteps.createNewUser(user);
        ValidatableResponse response = actionSteps.loginUser(credentials);
        accessToken = response.extract().path("accessToken").toString();
        actionSteps.createOrderOfFamousUser(accessToken);
        actionSteps.getFamousUserOrders(accessToken);
    }

    @Test
    @DisplayName("Получение списка заказов без регистрации")
    @Description("Проверка получения ошибки с кодом 401 при получении заказа не авторизованного пользователя")
    public void getOrderNoUserTest() {
        ValidatableResponse response = actionSteps.getUnknownUserOrders();
        validationUserSteps.createOrderWithoutUser(response);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            actionSteps.deleteUser(accessToken);
        }
    }
}
