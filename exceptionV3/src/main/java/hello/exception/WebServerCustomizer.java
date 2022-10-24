package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    //에러 페이지  커스터 마이징
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage400 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        //런타임 자식 예외까지
        ErrorPage errorPage = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage,errorPage400,errorPage500);

    }
}
