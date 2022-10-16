package hello.login;


import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

//우리가 만든 필터 등록하기
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") //전체경로
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 제외경로
    }

    //서블릿 등록
    //@Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 필터 등록
        filterRegistrationBean.setOrder(1); // 필터가 여러개 일수도 있으니 순서
        filterRegistrationBean.addUrlPatterns("/*"); // 해당 URL에 필터를 적용한다
        return filterRegistrationBean;
    }

    //서블릿 등록
    @Bean
    public FilterRegistrationBean logCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter()); // 필터 등록
        filterRegistrationBean.setOrder(2); // 필터가 여러개 일수도 있으니 순서
        filterRegistrationBean.addUrlPatterns("/*"); // 해당 URL에 필터를 적용한다
        return filterRegistrationBean;
    }

}
