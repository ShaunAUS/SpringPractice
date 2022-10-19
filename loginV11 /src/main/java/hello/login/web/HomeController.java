package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentResolver.Login;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;
    

    //    @GetMapping("/")
    public String home() {
        return "home";
    }


    // 로그인 후 화면 = 쿠키값 꺼내기
    //required = false - > 로그인 안한 사람도 접속할수는 있어야 하니까
    //@GetMapping("/")
    public String homeLoginV1(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        //쿠키가 없으면
        if (memberId == null) {
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }
        //로그인 정보 뷰단으로 넘기기
        model.addAttribute("member", loginMember);
        return "loginHome";
    }


    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        if (member == null) {
            return "home";
        }

        //로그인
        model.addAttribute("member", member);
        return "loginHome";

    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {


        //true 가 default
        // 처음 로그인 하는 사용자는 세션 만들필요가 없기때문에 false로 해준다 안그러면 메모리 손실
        HttpSession session = request.getSession(false);
        if(session == null){
            return "home";
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 값없으면 홈으로
        if (member == null) {
            return "home";
        }

        //세션 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";

    }

    //@GetMapping("/")
    public String homeLoginV4(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false)Member member, Model model) {

        //세션에 값없으면 홈으로
        if (member == null) {
            return "home";
        }

        //세션 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginArgumentResolver(@Login Member member, Model model) {

        //세션에 값없으면 홈으로
        if (member == null) {
            return "home";
        }

        //세션 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }

}