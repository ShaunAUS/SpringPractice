package hello.itemservice.validation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){

        String[] message = codesResolver.resolveMessageCodes("required","item");
        Assertions.assertThat(message).containsExactly("required.item","required");
        for (String s : message) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void messageCodesResolverField(){

        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

        // rejectValue 는 내부적으로 messageCodesResolver 사용
        //reject를 하면 fieldError에 에러메세지를 담는다
        //bindingResult.rejectValue("itemName" ,"required");

    }
}
