package User;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ValidationUserSteps {
    @Step("Проверка ответа успешного создания пользователя")
    public void userResponsePositive(ValidatableResponse response, int code, Boolean status) {
        response
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка ответа при создании юзера без email, password или name")
    public void userResponseNegative(ValidatableResponse response) {
        response
                .statusCode(403)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка ответа при попытке повторного создания существующего пользователя")
    public void createAnExistingUser(ValidatableResponse response) {
        response
                .statusCode(403)
                .and()
                .assertThat()
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка ответа при ошибке ввода email или password")
    public void incorrectCredentials(ValidatableResponse response) {
        response
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка ответа при получения заказа без юзера")
    public void updateWithoutUser(ValidatableResponse response) {
        response
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));

    }

    @Step("Проверка ответа при создании заказа без юзера")
    public void createOrderWithoutUser(ValidatableResponse response) {
        response
                .statusCode(401)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка ответа успешного создания заказа")
    public static void createOrderResponse(ValidatableResponse response, int code, Boolean status) {
        response
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }
}
