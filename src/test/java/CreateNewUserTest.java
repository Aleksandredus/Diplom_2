import User.*;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateNewUserTest {
    private User user;
    private ActionSteps actionSteps;
    private ValidationUserSteps validationUserSteps;
    private String accessToken;

    @Before
    public void setUser() {
        user = UserMaker.random();
        actionSteps = new ActionSteps();
        validationUserSteps = new ValidationUserSteps();
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Cоздаём пользователя и проверяем ответ об успешном выполнении операции")
    public void creatureUserPositiveTest() {
        ValidatableResponse response = actionSteps.createNewUser(user);
        accessToken = response.extract().path("accessToken").toString();
        validationUserSteps.userResponsePositive(response);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка получения ответа с кодом 403 при создании юзера без имени")
    public void creatureUserWithoutNameTest() {
        user.setName("");
        ValidatableResponse response = actionSteps.createNewUser(user);
        validationUserSteps.userResponseNegative(response);
    }

    @Test
    @DisplayName("Cоздание пользователя без email")
    @Description("Проверка получения ответа с кодом 403 при создании юзера без email")
    public void creatureUserWithoutEmailTest() {
        user.setEmail("");
        ValidatableResponse response = actionSteps.createNewUser(user);
        validationUserSteps.userResponseNegative(response);
    }

    @Test
    @DisplayName("Cоздание пользователя без пароля")
    @Description("Проверка получения ответа с кодом 403 при создании юзера без пароля")
    public void creatureUserWithoutPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = actionSteps.createNewUser(user);
        validationUserSteps.userResponseNegative(response);
    }

    @Test
    @DisplayName("Создание пользователя существующего в базе")
    @Description("Проверка получения ответа User already exists с кодом 403 при попытке создания существующего пользователя в системе")
    public void creatureUserDoubleTest() {
        actionSteps.createNewUser(user);
        ValidatableResponse response = actionSteps.createNewUser(user);
        validationUserSteps.createAnExistingUser(response);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            actionSteps.deleteUser(accessToken);
        }
    }

}
