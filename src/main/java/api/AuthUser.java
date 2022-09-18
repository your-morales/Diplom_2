package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthUser {
  @Step("delete запрос на ручку "+ ApiConstants.USER)
  public Response apiAuthUserDelete(String accessToken) {
    return given().auth().oauth2(accessToken).and().when().delete(ApiConstants.USER);
  }

  @Step("patch запрос на ручку " + ApiConstants.USER +" с авторизацией под клиентом")
  public Response apiAuthUserPatchWithAuth(String accessToken, Object userAccount) {
    return given().auth().oauth2(accessToken).header("Content-type", "application/json").and().body(userAccount).and().when().
            patch(ApiConstants.USER);
  }

  @Step("patch запрос на ручку " + ApiConstants.USER + " без авторизацией под клиентом")
  public Response apiAuthUserPatchWithoutAuth(Object userAccount) {
    return given().header("Content-type", "application/json").and().body(userAccount).and().when().
            patch(ApiConstants.USER);
  }
}
