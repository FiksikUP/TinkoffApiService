package tinkoffapiservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({StockNotFoundException.class})
    public ResponseEntity<ServiceException> handleNotFound(Exception e){
        return new ResponseEntity<>(new ServiceException(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
