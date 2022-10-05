package hello.itemservice.web.validation.form;


import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateForm {


    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 100 , max =  10000)
    private Integer price;

    //수정에서 수량은 자유롭게 변경가능
    private Integer quantity;
}
