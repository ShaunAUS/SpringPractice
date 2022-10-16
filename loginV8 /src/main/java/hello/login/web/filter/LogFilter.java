package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("log filter doFilter");

        //HttpServeltRequest 자식이 기능 더많아서 캐스팅 해줌
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        //모든 URL 로그 남기기
        String requestURI = httpRequest.getRequestURI();

        //요청 구분하기 위해
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST  [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response);   // FIXME 이게 있어야 다음 필터로 넘어감 없으면  -> 서블릿 ->컨트룰러 호출
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }


    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
