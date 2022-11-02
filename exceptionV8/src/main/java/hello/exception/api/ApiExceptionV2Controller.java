package hello.exception.api;

import hello.exception.excpetion.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@ExceptionHanlder 는 이 컨트룰러에만 영향을 준다
@Slf4j
@RestController
public class ApiExceptionV2Controller  {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //예외가 터지면 -> ExceptionHandlerResolver 에서 이것부터 우선으로 확인해서 잡는다
    //기존 문제 였던 JSON 으로 바꾸는것과, 정상처리 200 으로 바꿔주는 문제를 한번에 해결함
    //Illegar 자식까지 잡아줌
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegarExHandler(IllegalArgumentException e){
        log.error("");
        return new ErrorResult("BAD",e.getMessage());

    }

    //UserException 을 상속받은 자식까지 잡아줌
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }


    //위데 두개 EexceptionHandler 에서 못잡은것들을 잡아줌
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }


    @GetMapping("/api2/members/{id}")
    public ApiExceptionController.MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if(id.equals("user-ex")){
            throw new UserException("사용자오류");
        }
        return new ApiExceptionController.MemberDto(id, "hello " + id);
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
