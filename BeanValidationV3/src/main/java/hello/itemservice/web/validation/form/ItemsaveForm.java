package hello.itemservice.web.validation.form;


import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemsaveForm {


    @NotBlank()
    private String itemName;

    @NotNull()
    @Range(min = 100 , max =  10000)
    private Integer price;

    @NotNull()
    @Max(value = 999)
    private Integer quantity;
}
