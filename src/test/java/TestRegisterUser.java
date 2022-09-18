import api.AuthLogin;
import api.AuthRegister;
import api.AuthUser;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class TestRegisterUser {
  public static String accessToken;
  public static AuthRegister apiAuthRegister = new AuthRegister();
  public static AuthUser apiAuthUser = new AuthUser();
  public static AuthLogin apiAuthLogin = new AuthLogin();
  public final static String BASEURL = "https://stellarburgers.nomoreparties.site";
  public static UserAccount userAccount = new UserAccount("guns1129111@yandex.ru", "password123", "Andrey");

  @Before
  public void setUp() {
    RestAssured.baseURI = BASEURL;
  }

  @After
  public void deleteUserAfterTest() {
    deleteUser();
  }

  @Step("Удаление пользователя")
  public static void deleteUser() {
    Response authLogin = apiAuthLogin.apiAuthLoginPost(userAccount);
    if (authLogin.then().extract().statusCode() == 200) {
      getAccessToken();
      Response authDelete = apiAuthUser.apiAuthUserDelete(accessToken);
      authDelete.then().statusCode(202).and().assertThat().body("success", equalTo(true))
              .and().assertThat().body("message", equalTo("User successfully removed"));
    }
  }

  @Step("Получение токена при логине клиента")
  public static void getAccessToken() {
    Response authLogin = apiAuthLogin.apiAuthLoginPost(userAccount);
    if (authLogin.then().extract().statusCode() == 200) {
      String accessTokenString = authLogin.then().statusCode(200).and().extract().body().path("accessToken");
      accessToken = accessTokenString.substring(accessTokenString.indexOf(' ') + 1);
    }
  }

  @DisplayName("Успешное создание пользователя, проверка кода успешного ответа и тела ответа об успешном создании пользователя")
  @Test
  public void testCreateUser() {
    Response createUser = createUser();
    createUser.then().statusCode(200).and().assertThat().body("success", equalTo(true))
            .and().assertThat().body("user.email", equalTo("guns1129111@yandex.ru"))
            .and().assertThat().body("user.name", equalTo("Andrey"));
  }

  @DisplayName("Проверка создания пользователя, который уже зарегестрирован")
  @Test
  public void testErrorWhenCreateExistingUser() {
    createUser();
    Response errorWhenCreateExistingUser = apiAuthRegister.apiAuthRegister(userAccount);
    errorWhenCreateExistingUser.then().statusCode(403)
            .and().assertThat().body("success", equalTo(false))
            .and().assertThat().body("message", equalTo("User already exists"));
  }

  @Step("Создание пользователя")
  public static Response createUser() {
    return apiAuthRegister.apiAuthRegister(userAccount);
  }

  @DisplayName("Проверка создания пользователя без обязательного поля емейла")
  @Test
  public void testErrorWhenCreateUserWithoutEmail() {
    UserAccount userAccountWithoutEmail = new UserAccount(null, "password123", "Andrey");
    Response errorWhenCreateUserWithoutEmail = apiAuthRegister.apiAuthRegister(userAccountWithoutEmail);
    errorWhenCreateUserWithoutEmail.then().statusCode(403)
            .and().assertThat().body("success", equalTo(false))
            .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
  }

  @DisplayName("Проверка создания пользователя без обязательного поля пароль")
  @Test
  public void testErrorWhenCreateUserWithoutPassword() {
    UserAccount userAccountWithoutPassword = new UserAccount("guns1129111@yandex.ru", null, "Andrey");
    Response errorWhenCreateUserWithoutPassword = apiAuthRegister.apiAuthRegister(userAccountWithoutPassword);
    errorWhenCreateUserWithoutPassword.then().statusCode(403)
            .and().assertThat().body("success", equalTo(false))
            .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
  }

  @DisplayName("Проверка создания пользователя без обязательного поля имя")
  @Test
  public void testErrorWhenCreateUserWithoutName() {
    UserAccount userAccountWithoutName = new UserAccount("guns1129111@yandex.ru", "password123", null);
    Response errorWhenCreateUserWithoutName = apiAuthRegister.apiAuthRegister(userAccountWithoutName);
    errorWhenCreateUserWithoutName.then().statusCode(403)
            .and().assertThat().body("success", equalTo(false))
            .and().assertThat().body("message", equalTo("Email, password and name are required fields"));
  }
}
