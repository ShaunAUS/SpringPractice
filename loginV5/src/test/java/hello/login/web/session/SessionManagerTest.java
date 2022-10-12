package hello.login.web.session;


import hello.login.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

    // 세션 id , object 저장하고 세션 Id를 쿠키에 담아 클라에게 보낸다
    @Test
    void sessionTest() {

        SessionManager sessionManager = new SessionManager();

        //세션 생성  = 클라한테 던져주기
        // Mock 이걸로 가짜 response 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);


        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());    // response 서버로부터 온 쿠키값을 넣어서 다시 보내준다


        //세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }

}
