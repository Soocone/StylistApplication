package musinsa.util;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final int status;

    public CustomException(String message, int status) {
        super(message);
        this.status = status;
    }

}
