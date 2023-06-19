package User;

import org.apache.commons.lang3.RandomStringUtils;

public class UserMaker {
    public static User generic() {
        return new User("gvozdkov1993@yandex.ru", "qwer1234", "Aleksandr");
    }

    public static User random() {
        return new User(RandomStringUtils.randomAlphabetic(10) + "@example.com", "Test", "Unknown");
    }
}
