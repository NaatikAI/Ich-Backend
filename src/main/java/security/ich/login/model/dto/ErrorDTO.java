package security.ich.login.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class ErrorDTO {
    private Date timestamp;
    private String details;
    private String message;

}
