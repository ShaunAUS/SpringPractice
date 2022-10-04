package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

@Component
public class ItemValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //  Item == clazz
    }

    //Errors = BindingReuslt 의 부모클래스

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        //bindingResult는 이미 뭐가 target인지 알고 있으므로 objectName 생략 가능
        //errorcode 는 앞글자만 사용
        // 디테일한 경로 에러메세지가 있으면 그것부터 우선출력  -> 내부적으로 MessageCodesResolver사용
        if(!StringUtils.hasText(item.getItemName())){
            errors.rejectValue("itemName" ,"required");
        }


        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice()>10000){
            errors.rejectValue("price","range",new Object[]{1000,10000},null);
        }

        //복합적인 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice <10000){
                // bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
                errors.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }


    }
}
