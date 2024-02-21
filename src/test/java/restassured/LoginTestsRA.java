package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

public class LoginTestsRA {
    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }
    String endpoint = "user/login/usernamepassword";
    @Test
    public void loginSuccess(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("chara@gmail.com")
                .password("Chara12345$")
                .build();
        AuthResponseDTO responseDTO = given()
                .body(auth)
                .contentType("application/json")
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDTO.class);
        System.out.println(responseDTO.getToken());

    }

    @Test
    public void loginWrongEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("maragmail.com")
                .password("Mmar123456$")
                .build();
        ErrorDTO errorDTO = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDTO.class);
        Assert.assertEquals(errorDTO.getMessage(),"Login or Password incorrect");
    }
}
