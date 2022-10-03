package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
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

import java.sql.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    //컨트룰러 호출될때마다 검증기 생성
    //이 컨트룰러에만 적용
    //글로벌 적용시 필요 x
    @InitBinder
    public void init(WebDataBinder webDataBinder){
        webDataBinder.addValidators(itemValidator);
    }

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

   // @PostMapping("/add")

    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {



        //검증
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName","상품명은 필수 입니다"));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice()>10000){
            bindingResult.addError(new FieldError("item","price","가격은 ~이상입니다"));
        }

        //복합적인 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice <10000){
                bindingResult.addError(new ObjectError("item","오류메세지"));
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
        return "redirect:/validation/v2/items/{itemId}";
    }


    /**
     * BindingResult 는 ModelAttribute 다음에 와야됀다
     */
    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {



        //검증
        //세번째 메서드는 클라이언트가 검증실패를 유도하는 값, 네번째는 binding이 잘됐는지 안됐는지
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,null,null,"상품명은 필수 입니다"));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice()>10000){
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,null,null,"가격은 ~이상입니다"));
        }

        //복합적인 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice <10000){
                bindingResult.addError(new ObjectError("item", null,null,"오류메세지"));
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
        return "redirect:/validation/v2/items/{itemId}";
    }

    //오류메세지 커스터마이징
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {



        //검증
        //세번째 메서드는 properties에서 오류메세지 가져옴
        //에러메세지는 여러개가 들어갈수 있다.
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null,null));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice()>10000){
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,10000},null));
        }

        //복합적인 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice <10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
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
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


        //bindingResult는 이미 뭐가 target인지 알고 있으므로 objectName 생략 가능
        //errorcode 는 앞글자만 사용
        // 디테일한 경로 에러메세지가 있으면 그것부터 우선출력  -> 내부적으로 MessageCodesResolver사용
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.rejectValue("itemName" ,"required");
        }


        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice()>10000){
           bindingResult.rejectValue("price","range",new Object[]{1000,10000},null);
        }

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
        return "redirect:/validation/v2/items/{itemId}";
    }

    //Validator 분리
   //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증로직
        itemValidator.validate(item,bindingResult);

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
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@Validated = 검증기를 실행하라는 애노테이션
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /*//검증로직
        itemValidator.validate(item,bindingResult);*/

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
        return "redirect:/validation/v2/items/{itemId}";
    }






    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

