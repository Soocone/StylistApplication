package musinsa.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int status;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = true;
        res.data = data;
        res.status = 200;
        return res;
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = false;
        res.message = message;
        res.status = status;
        return res;
    }
}
