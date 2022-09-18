package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Orders {
  @Step("post запрос на ручку " + ApiConstants.ORDERS_ENDPOINT + " для создания заказа под авторизированным пользователем")
  public Response apiOrdersCreateOrderWithAuth(String accessToken, Object order) {
    return given().auth().oauth2(accessToken).header("Content-type", "application/json").and().body(order).and().when().
            post(ApiConstants.ORDERS_ENDPOINT);
  }

  @Step("post запрос на ручку " + ApiConstants.ORDERS_ENDPOINT + " для создания заказа под неавторизированным пользователем")
  public Response apiOrdersCreateOrderWithoutAuth(Object order) {
    return given().header("Content-type", "application/json").and().body(order).and().when().
            post(ApiConstants.ORDERS_ENDPOINT);
  }

  @Step("post запрос на ручку " + ApiConstants.ORDERS_ENDPOINT + " для создания заказа под авторизированным пользователем")
  public Response apiGetOrdersWithAuth(String accessToken) {
    return given().auth().oauth2(accessToken).header("Content-type", "application/json").and().when().
            get(ApiConstants.ORDERS_ENDPOINT);
  }

  @Step("post запрос на ручку " + ApiConstants.ORDERS_ENDPOINT + " для создания заказа под неавторизированным пользователем")
  public Response apiGetOrdersWithoutAuth() {
    return given().header("Content-type", "application/json").and().when().
            get(ApiConstants.ORDERS_ENDPOINT);
  }
}
