package security.ich.login.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
public class AppException extends RuntimeException  {
    private HttpStatus status;
    private String message;
}
