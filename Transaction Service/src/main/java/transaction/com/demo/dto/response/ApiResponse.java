package transaction.com.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private boolean success;
    private String errorMessage;
    private HttpStatus httpStatus;
    private String response;
    private Long responseTime;
    private Object data;
}
