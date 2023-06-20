import User.*;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserAutorizationTest {
    private User user;
    private ActionSteps actionSteps;
    private Credentials credentials;
    private ValidationUserSteps validationUserSteps;
    private String accessToken;
    private int code;
    private boolean statys;

    @Before
    public void setUser() {
        user = UserMaker.random();
        actionSteps = new ActionSteps();
        validationUserSteps = new ValidationUserSteps();
        credentials = new Credentials(user);
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверка полученного ответа при успешной авторизации пользователя")
    public void authorizationUser() {
        actionSteps.createNewUser(user);
        ValidatableResponse response = actionSteps.loginUser(credentials);
        accessToken = response.extract().path("accessToken").toString();
        validationUserSteps.userResponsePositive(response, code, statys);
    }

    @Test
    @DisplayName("Авторизация пользователя без логина")
    @Description("Проверка полученного ответа с кодом 401 при авторизации пользователя без логина")
    public void authorizationUserWithoutEmail() {
        user.setEmail("");
        ValidatableResponse response = actionSteps.loginUser(credentials);
        validationUserSteps.incorrectCredentials(response);
    }

    @Test
    @DisplayName("Авторизация пользователя без пароля")
    @Description("Проверка полученного ответа с кодом 401 при авторизации пользователя без пароля")
    public void authorizationUserWithoutPassword() {
        user.setPassword("");
        ValidatableResponse response = actionSteps.loginUser(credentials);
        validationUserSteps.incorrectCredentials(response);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            actionSteps.deleteUser(accessToken);
        }
    }
}
