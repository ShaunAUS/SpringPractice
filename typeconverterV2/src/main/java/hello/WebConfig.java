package hello;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//컨버터 등록
//스프링 내부에는 이미 수많은 컨버터가 존재해 사실 이렇게 등록 필요 없음  하지만 추가한 컨버터가 우선순위를 갖는다.
@Configuration
public class WebConfig implements WebMvcConfigurer {

        //Converter보다 확장된 기능
        @Override
        public void addFormatters(FormatterRegistry registry){
            registry.addConverter(new StringToIntegerConverter());
            registry.addConverter(new IntegerToStringConverter());
            registry.addConverter(new StringToIpPortConverter());
            registry.addConverter(new IpPortToStringConverter());

    }
}
