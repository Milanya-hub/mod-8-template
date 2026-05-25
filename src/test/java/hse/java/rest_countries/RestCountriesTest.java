package hse.java.rest_countries;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestCountriesTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://restcountries.com/v3.1";
    }

    @Test
    @DisplayName("GET /all returns non-empty")
    void getAllCountriesShouldReturnNonEmptyList() {
        given()// создаем и настраиваем http-запрос
                .queryParam("fields", "name")
                .when() // выполняем
                .get("/all")// отправка get-запроса
                .then()// проверяем
                .statusCode(200)// все ок
                .body("$", not(empty()));// проверяем lson-отыет
    }

    @Test
    @DisplayName("GET /name/russia contains capital Moscow")
    void russiaShouldHaveCapitalMoscow() {
        given()
                .when()
                .get("/name/russia")
                .then()
                .statusCode(200)
                .body("[0].capital", hasItem("Moscow"));
    }

    @Test
    @DisplayName("GET /alpha/de returns Germany")
    void alphaDeShouldReturnGermany() {
        given()
                .when()
                .get("/alpha/de")
                .then()
                .statusCode(200)
                .body("[0].name.common", equalTo("Germany"));
    }

    @Test
    @DisplayName("GET nonexistent country returns 404")
    void nonexistentCountryShouldReturn404() {
        given()
                .when()
                .get("/name/nonexistentcountryxyz")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("All countries should have population > 0")
    void allCountriesShouldHavePositivePopulation() {
        Response response = given()
                .queryParam("fields", "population")
                .when()
                .get("/all");

        response.then().statusCode(200);

        List<Map<String, Object>> countries = response.jsonPath().getList("$");

        for (Map<String, Object> country : countries) {
            Number population = (Number) country.get("population");
            assert population != null;
            assert population.longValue() >= 0;
        }
    }

    // мое
    @Test
    @DisplayName("France should contain Paris as capital")
    void franceShouldHaveCapitalParis() {
        given()
                .when()
                .get("/name/france")
                .then()
                .statusCode(200)
                .body("[0].capital", hasItem("Paris"));
    }

    @Test
    @DisplayName("Japan should have region Asia")
    void japanShouldBeInAsia() {
        given()
                .when()
                .get("/name/japan")
                .then()
                .statusCode(200)
                .body("[0].region", equalTo("Asia"));
    }
}