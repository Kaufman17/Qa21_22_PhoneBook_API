package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//{
//        "contacts": [
//        {
//        "id": "string",
//        "name": "string",
//        "lastName": "string",
//        "email": "string",
//        "phone": "321979355048593",
//        "address": "string",
//        "description": "string"
//        }
//        ]
//        }
@Getter
@Setter
@ToString
@Builder

public class ContactDTO {

    private String id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String description;

}
