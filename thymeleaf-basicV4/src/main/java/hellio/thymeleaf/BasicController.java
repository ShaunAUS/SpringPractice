package hellio.thymeleaf;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("text-basic")
    public String textBasic(Model model){
        model.addAttribute("data","Hello Spring!");
        return "basic/text-basic";
    }


    @GetMapping("text-unescaped")
    public String textUnescaped(Model model){
        model.addAttribute("data","Hello <b>Spring</b>!");
        return "basic/text-unescaped";
    }


    @GetMapping("variable")
    public String variable(Model model){

        User userA = new User("userA", 10);
        User userB = new User("userB", 20);

        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        Map<String,User> map = new HashMap<>();
        map.put("userA",userA);
        map.put("userB",userB);

        model.addAttribute("user",userA);
        model.addAttribute("users",list);
        model.addAttribute("userMap",map);


        return "basic/variable";
    }

    @GetMapping("/basic-objects")
    public String basicObjects(HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");
        return "basic/basic-objects";
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello " + data;
        }}

    /**
     * 유틸 , 날짜
     * @param model
     * @return
     */
    @GetMapping("date")
    static  String Date(Model model){
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    /**
     * 링크
     * @param model
     * @return
     */

    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";
    }

    /**
     *  리터럴
     * @param model
     * @return
     */

    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("data", "Spring!");
        return "basic/literal";
    }

    /**
     * 연산
     * @param model
     * @return
     */

    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "Spring!");
        return "basic/operation";
    }

    /**
     * 속성 값 설정
     * @return
     */
    @GetMapping("/attribute")
    public String attribute() {
        return "basic/attribute";
    }


    /**
     * 반복문
     * @param model
     * @return
     */
    @GetMapping("/each")
    public String each(Model model) {
        addUsers(model);
        return "basic/each";
    }
    private void addUsers(Model model) {
        List<User> list = new ArrayList<>();
        list.add(new User("userA", 10));
        list.add(new User("userB", 20));
        list.add(new User("userC", 30));
        model.addAttribute("users", list);
    }


    /**
     * 조건부 평가
     * @param model
     * @return
     */
    @GetMapping("/condition")
    public String condition(Model model) {
        addUsers(model);
        return "basic/condition";
    }

    /**
     * 주석
     * @param model
     * @return
     */
    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("data", "Spring!");
        return "basic/comments";
    }

    /**
     * 블록
     */
    @GetMapping("/block")
    public String block(Model model) {
        addUsers(model);
        return "basic/block";
    }

    /**
     * 자바스크립트
     */

    @GetMapping("/javascript")
    public String javascript(Model model) {
        model.addAttribute("user", new User("userA", 10));
        addUsers(model);
        return "basic/javascript";
    }


    @Data
    static class User{
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
