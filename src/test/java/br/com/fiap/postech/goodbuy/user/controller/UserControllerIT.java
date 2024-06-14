package br.com.fiap.postech.goodbuy.user.controller;

import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.entity.enums.UserRole;
import br.com.fiap.postech.goodbuy.user.helper.UserHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class UserControllerIT {

    public static final String USER = "/goodbuy/user";
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class CadastrarUser {
        @Test
        void devePermitirCadastrarUser() {
            var user = UserHelper.getUser(false);
            user.setLogin(user.getLogin() + "!!!");
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(user)
            .when()
                .post(USER)
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("schemas/user.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCadastrarUser_RequisicaoXml() {
            given()
                .contentType(MediaType.APPLICATION_XML_VALUE)
            .when()
                .post(USER)
            .then()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"));
        }
    }

    @Nested
    class BuscarUser {
        @Test
        void devePermitirBuscarUserPorId() {
            var id = "7a04f6fb-c79b-4b47-af54-9bef34cbab35";
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(USER + "/{id}", id)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/user.schema.json"));
        }
        @Test
        void devePermitirBuscarUserPorLogin() {
            var login = "anderson.wagner";
            given()
                    //.header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get(USER + "/findByLogin/{login}", login)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/user.schema.json"));
        }
        @Test
        void deveGerarExcecao_QuandoBuscarUserPorId_idNaoExiste() {
            var id = UserHelper.getUser(true).getId();
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(USER + "/{id}", id)
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void devePermitirBuscarTodosUser() {
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(USER)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/user.page.schema.json"));
        }

        @Test
        void devePermitirBuscarTodosUser_ComPaginacao() {
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                .queryParam("page", "1")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .get(USER)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/user.page.schema.json"));
        }
    }

    @Nested
    class AlterarUser {
        @Test
        void devePermitirAlterarUser(){
            var user = new User(
                    "kaiby.santos",
                    "Kaiby o mestre do miro !!!",
                    "52816804046",
                    null,
                    null
            );
            user.setId(UUID.fromString("c5ce37f4-3160-48d0-bd89-1d680ff77808"));
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                .body(user).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put(USER + "/{id}", user.getId())
            .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body(matchesJsonSchemaInClasspath("schemas/user.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarUser_RequisicaoXml() {
            var user = UserHelper.getUser(true);
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                .body(user).contentType(MediaType.APPLICATION_XML_VALUE)
            .when()
                .put(USER + "/{id}", user.getId())
            .then()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        }

        @Test
        void deveGerarExcecao_QuandoAlterarUserPorId_idNaoExiste() {
            var user = UserHelper.getUser(true);
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
                .body(user).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put(USER + "/{id}", user.getId())
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("User não encontrado com o ID: " + user.getId()));
        }
    }

    @Nested
    class RemoverUser {
        @Test
        void devePermitirRemoverUser() {
            var user = new User();
            user.setId(UUID.fromString("f6497965-3cf0-4601-a631-01878ef70f40"));
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
            .when()
                .delete(USER + "/{id}", user.getId())
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void deveGerarExcecao_QuandoRemoverUserPorId_idNaoExiste() {
            var user = UserHelper.getUser(true);
            given()
                .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken())
            .when()
                .delete(USER + "/{id}", user.getId())
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(equalTo("User não encontrado com o ID: " + user.getId()));
        }

        @Test
        void deveGerarExcecao_QuandoRemoverUserPorId_roleUser() {
            var user = new User(
                    "usuario.comum",
                    "Jose da Silva",
                    "'07379758063'",
                    null,
                    UserRole.USER
            );
            user.setId(UUID.fromString("3929bac7-149a-443d-9a86-5afec529aaba"));
            var response = given()
                    .header(HttpHeaders.AUTHORIZATION, UserHelper.getToken(user))
            .when()
                    .delete(USER + "/{id}", user.getId())
            .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .extract().jsonPath();

            var error = response.get("error");
            assertThat(error).isEqualTo("Forbidden");
        }
    }
}
