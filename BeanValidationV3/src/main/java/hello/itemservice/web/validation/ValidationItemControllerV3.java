package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

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


    //@Validated = 검증기를 실행하라는 애노테이션
    //@PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


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

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@Validated = 검증기를 실행하라는 애노테이션
    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class)  @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {


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

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

   // @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {


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
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {


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
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

