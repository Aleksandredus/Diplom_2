
import User.*;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangeUserSettingsTest {
    private User userForUpdate;
    private ActionSteps actionSteps;
    private ValidationUserSteps validationUserSteps;


    @Before
    public void setUser() {
        userForUpdate = UserMaker.random();
        actionSteps = new ActionSteps();
        validationUserSteps = new ValidationUserSteps();
    }

    @Test
    @DisplayName("Изменение данных зарегестрированого пользователя")
    @Description("Проверка получения успешного ответа при изменении данных зарегистрированного пользователя")
    public void updateUser() {
        User initialUser = UserMaker.random();
        User userForUpdate = initialUser.clone();
        userForUpdate.setEmail(RandomStringUtils.randomAlphabetic(10) + "@newexample.com");
        String accessToken = actionSteps.createNewUser(initialUser).extract().header("Authorization");
        actionSteps.updateUser(accessToken, userForUpdate);
        ValidatableResponse updatedUserResponse = actionSteps.getUserInfo(accessToken);
        updatedUserResponse.body("user.name", equalTo(initialUser.getName())).and().body("user.email", equalTo(userForUpdate.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Изменения данных не зарегестрированого пользователя")
    @Description("Проверка получения ответа с кодом 401 при изменении данных неизвестного пользователя")
    public void updateUnknownUser() {
        userForUpdate.setEmail(RandomStringUtils.randomAlphabetic(10) + "@newexample.com");
        ValidatableResponse response = actionSteps.updateUser("", userForUpdate);
        validationUserSteps.updateWithoutUser(response);
    }

}
