package hello.exception.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//ResponseStatusExceptionResolver
//500 -> 400
//reason -> properties 메세지 정리 가능
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason =  "error.bad")
public class BadRequestException extends RuntimeException{
}
