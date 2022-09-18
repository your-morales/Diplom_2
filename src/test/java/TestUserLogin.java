import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class TestUserLogin extends TestRegisterUser {
  public UserAccount userLogin;

  @DisplayName("Успешный вход под созданным пользователем")
  @Test
  public void testSuccessfulLoginUser() {
    createUser();
    userLogin = new UserAccount("guns1129111@yandex.ru", "password123");
    Response loginUser = TestRegisterUser.apiAuthLogin.apiAuthLoginPost(userLogin);
    loginUser.then().statusCode(200).
            and().assertThat().body("success", equalTo(true)).
            and().assertThat().body("user.email", equalTo("guns1129111@yandex.ru")).
            and().assertThat().body("user.name", equalTo("Andrey"));
  }

  @DisplayName("Проверка ошибка при некорректном вводе емейла при логине")
  @Test
  public void testSErrorWhenInvalidEmail() {
    createUser();
    userLogin = new UserAccount("guns1129111@yandex.ru1121", "password123");
    Response invalidEmail = TestRegisterUser.apiAuthLogin.apiAuthLoginPost(userLogin);
    invalidEmail.then().statusCode(401).
            and().assertThat().body("success", equalTo(false)).
            and().assertThat().body("message", equalTo("email or password are incorrect"));
  }

  @DisplayName("Проверка ошибка при некорректном вводе пароля при авторизации")
  @Test
  public void testSErrorWhenInvalidPasword() {
    createUser();
    userLogin = new UserAccount("guns1129111@yandex.ru", "password123456");
    Response invalidPassword = TestRegisterUser.apiAuthLogin.apiAuthLoginPost(userLogin);
    invalidPassword.then().statusCode(401).
            and().assertThat().body("success", equalTo(false)).
            and().assertThat().body("message", equalTo("email or password are incorrect"));
  }
}