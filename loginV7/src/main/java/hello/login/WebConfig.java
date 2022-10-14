package hello.login;


import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

//우리가 만든 필터 등록하기
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 필터 등록
        filterRegistrationBean.setOrder(1); // 필터가 여러개 일수도 있으니 순서
        filterRegistrationBean.addUrlPatterns("/*"); // 해당 URL에 필터를 적용한다
        return filterRegistrationBean;
    }

}
