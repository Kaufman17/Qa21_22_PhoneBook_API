package okhttp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.ContactDTO;
import dto.DeleteByIdResponseDTO;
import dto.ErrorDTO;
import dto.GetAllContactsDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class DeleteContactByIDOkhttp {
    String id;
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiY2hhcmFAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3MDg5NzA5MDMsImlhdCI6MTcwODM3MDkwM30.PqMj74yHrFwOPIKtx3wwaeYjCq6uZaxYdiMkNGI1QCo";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact
        //get id from "message":"Contact was added! ID: 55468539-b62c-41b6-b333-8e8763b76c15"
        ContactDTO contact = ContactDTO.builder()
                .name("Markel")
                .lastName("Simpson")
                .email("bart@gmail.com")
                .phone("32145698712")
                .address("NY")
                .description("Friend")
                .build();
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        String responseBodyString = response.body().string();
        System.out.println("JSON Response: " + responseBodyString);


        GetAllContactsDTO contactsDTO = gson.fromJson(responseBodyString, GetAllContactsDTO.class);
        List<ContactDTO> contacts = contactsDTO.getContacts();


        String id = null;
        for (ContactDTO c : contacts) {
            if (c.getName().equals("Markel")) {
                id = c.getId();
                break;
            }
        }

        if (id != null) {
            this.id = id;
            System.out.println("Extracted ID: " + id);
        } else {
            throw new RuntimeException("Failed to find the ID of the created contact.");
        }

}
    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);
        DeleteByIdResponseDTO dto = gson.fromJson(response.body().string(), DeleteByIdResponseDTO.class);
        System.out.println(dto.getMessage());
        Assert.assertEquals(dto.getMessage(), "Contact was deleted!");
    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/ec37070e-ec37-4c22-bee1-3a4eeb6f445c")
                .delete()
                .addHeader("Authorization", "ghjf")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");
    }

    @Test
    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + 123)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        System.out.println(errorDTO.getMessage());
        Assert.assertEquals(errorDTO.getMessage(), "Contact with id: 123 not found in your contacts!");
    }
}
