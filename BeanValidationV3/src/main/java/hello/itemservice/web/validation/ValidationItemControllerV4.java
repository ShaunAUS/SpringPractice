package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import hello.itemservice.web.validation.form.ItemsaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("//validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());  // 빈객체로 보내줘야 코드 재사용 가능
        return "validation/v1/addForm";
    }


    /**
     * @ModelAttribute("뷰템플릿에서 사용할 이름")
     */
    @ModelAttribute("뷰템플릿에서 사용할 이름")
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemsaveForm item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


        //복합적인 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice <10000){
                // bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }


        /**
         * bindingResult 는 Model에 안담아줘도 뷰단에 넘어감
         */
        if(bindingResult.hasErrors()){
            log.info("bindingResult = {}", bindingResult);
            return "validation/v1/addForm";
        }

        Item testItem =new Item();
        testItem.setItemName(item.getItemName());
        testItem.setPrice(item.getPrice());
        testItem.setQuantity(item.getQuantity());

        Item savedItem = itemRepository.save(testItem);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect://validation/v4/items/{itemId}";
    }


    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm item, BindingResult bindingResult) {


        //복합적인 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice <10000){
                // bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "/validation/v4/editForm";
        }

        Item updateItem =new Item();
        updateItem.setItemName(item.getItemName());
        updateItem.setPrice(item.getPrice());
        updateItem.setQuantity(item.getQuantity());

        itemRepository.update(itemId, updateItem);
        return "redirect://validation/v4/items/{itemId}";
    }

}

