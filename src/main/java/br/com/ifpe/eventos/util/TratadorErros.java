package br.com.ifpe.eventos.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorErros {

   @ExceptionHandler(Exception.class)
   public ResponseEntity tratarErro500(Exception ex) {
       System.err.println("Erro 500 capturado: " + ex.getMessage());
       ex.printStackTrace();
       
       Map<String, Object> errorResponse = new HashMap<>();
       errorResponse.put("error", "Erro interno do servidor");
       errorResponse.put("message", ex.getMessage());
       errorResponse.put("timestamp", System.currentTimeMillis());
       
       return ResponseEntity.internalServerError().body(errorResponse);
   }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("fieldName", ((FieldError) error).getField());
            errorDetails.put("defaultMessage", error.getDefaultMessage());
            errors.add(errorDetails);
        });
        
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
