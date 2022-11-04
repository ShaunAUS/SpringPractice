package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class converterTest {


    @Test
    void integerToString(){

        Integer i = 10;
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(i);
        assertThat(result).isEqualTo("10");
    }

    @Test
    void StringToInteger(){
        StringToIntegerConverter converter = new StringToIntegerConverter();
        assertThat(converter.convert("10")).isEqualTo(10);
    }

    @Test
    void stringToIpPort() {
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String source = "127.0.0.1:8080";
        IpPort result = converter.convert(source);
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
    }
    @Test
    void ipPortToString() {
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);
        String result = converter.convert(source);
        assertThat(result).isEqualTo("127.0.0.1:8080");

    }
}