package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder

public class ErrorDTO {
    private int status;
    private String error;
    private String path;
    private Object message;
}
