package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }


    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        log.info("login? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO return "redirect:/";
        return null;
    };


    /**
     * 쿠키로 로그인 구현
     */
    //@PostMapping("/login")
    public String loginV1(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        //오류 발생시 다시 로그인 페이지로
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());


        log.info("login? {}", loginMember);

        //글로벌 오류   id  or password가 맞지 않음 = 하나의 필드 오류가 아님
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //로그인 성공시 서버에서 쿠키를 넘겨준다
        // 시간을 정하지 않으면(세션쿠키) 브라우저 종료시 사라짐
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        //오류 발생시 다시 로그인 페이지로
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());


        log.info("login? {}", loginMember);

        //글로벌 오류   id  or password가 맞지 않음 = 하나의 필드 오류가 아님
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        // 세션 관리자를 통해 세션을 생성하고, 회원데이터 보관
        sessionManager.createSession(loginMember,response);
        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {

        //오류 발생시 다시 로그인 페이지로
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        log.info("login? {}", loginMember);

        //글로벌 오류   id  or password가 맞지 않음 = 하나의 필드 오류가 아님
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //세션이 있으면 세션반환 , 없으면 새로 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        //로그인 성공 처리
        // 세션 관리자를 통해 세션을 생성하고, 회원데이터 보관
        //세션 없이 만든 세션로직
        //sessionManager.createSession(loginMember,response);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute LoginForm form,
                          BindingResult bindingResult,
                          HttpServletRequest request,
                          @RequestParam(defaultValue = "/") String redirectURL
    ) {

        //오류 발생시 다시 로그인 페이지로
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        log.info("login? {}", loginMember);

        //글로벌 오류   id  or password가 맞지 않음 = 하나의 필드 오류가 아님
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //세션이 있으면 세션반환 , 없으면 새로 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        //로그인 성공 처리
        // 세션 관리자를 통해 세션을 생성하고, 회원데이터 보관
        //세션 없이 만든 세션로직
        //sessionManager.createSession(loginMember,response);
        return "redirect:" + redirectURL;
    }





    //로그아웃 - ->쿠키 시간종료 시키기
    //@PostMapping("/logout")
    public String logoutV1(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    //로그아웃 - ->세션 종료 시키기
    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    //세션종료
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }
    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
