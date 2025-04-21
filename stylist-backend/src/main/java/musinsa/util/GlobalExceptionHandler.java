package musinsa.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        // 항상 200 OK로 보내고, body에서 status/성공 여부 판단
        return ResponseEntity.ok(ApiResponse.error(ex.getMessage(), ex.getStatus()));
        // return ResponseEntity
        //       .status(ex.getStatus())
        //       body(ApiResponse.error(ex.getMessage(), ex.getStatus()));
    }

    // 그 외 예상 못한 예외 처리 (디버깅용)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ex.printStackTrace(); // 서버 콘솔 로그
        return ResponseEntity
                .status(500)
                .body(ApiResponse.error("서버 내부 오류가 발생했습니다.", 500));
    }
}
