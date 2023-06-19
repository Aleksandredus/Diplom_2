import User.ActionSteps;
import User.User;
import User.UserMaker;
import User.ValidationUserSteps;

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
    private int code;
    private boolean statys;

    @Before
    public void setUser() {
        user = UserMaker.random();
        actionSteps = new ActionSteps();
        validationUserSteps = new ValidationUserSteps();
    }

    @Test
    @DisplayName("Тестирование создание пользователя")
    public void creatureUserPositiveTest() {
        ValidatableResponse response = actionSteps.createNewUser(user);
        accessToken = response.extract().path("accessToken").toString();
        validationUserSteps.createUserResponsePositive(response, code, statys);
    }

    @Test
    @DisplayName("Тестирование создание пользователя без имени")
    public void creatureUserWithoutNameTest() {
        user.setName("");
        ValidatableResponse response = actionSteps.createNewUser(user);
        validationUserSteps.createUserResponseNegative(response);
    }

    @Test
    @DisplayName("Тестирование создание пользователя без email")
    public void creatureUserWithoutEmailTest() {
        user.setEmail("");
        ValidatableResponse response = actionSteps.createNewUser(user);
        validationUserSteps.createUserResponseNegative(response);
    }

    @Test
    @DisplayName("Тестирование создание пользователя без пароля")
    public void creatureUserWithoutPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = actionSteps.createNewUser(user);
        validationUserSteps.createUserResponseNegative(response);
    }

    @Test
    @DisplayName("Тестируем создание пользователя с одинаковым логином")
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
