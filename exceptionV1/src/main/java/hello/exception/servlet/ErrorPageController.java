package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class ErrorPageController {

    @RequestMapping("/error-page/404")
    public String error404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage404");
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String error500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage500");
        return "error-page/500";
    }
}
