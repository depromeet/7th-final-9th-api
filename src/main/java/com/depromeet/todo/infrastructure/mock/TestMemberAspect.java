package com.depromeet.todo.infrastructure.mock;

import com.depromeet.todo.domain.member.oauth.OAuthAccessToken;
import com.depromeet.todo.domain.member.oauth.OAuthUserInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Profile("test-member")
@Aspect
@Component
public class TestMemberAspect {
    private static final String TEST_MEMBER_ACCESS_TOKEN = "jibsuniAccessToken";
    private static final long TEST_MEMBER_ID = 1L;

    @Around("execution(* com.depromeet.todo.infrastructure.jwt.JwtService.decode(String))")
    public Object mockUser(ProceedingJoinPoint pjp) throws Throwable {
        try {
            String token = (String) pjp.getArgs()[0];
            if (TEST_MEMBER_ACCESS_TOKEN.equals(token)) {
                return Optional.of(TEST_MEMBER_ID);
            }
        } catch (Exception ignored) {
            // do nothing
        }
        return pjp.proceed();
    }

    @Around("execution(* com.depromeet.todo.infrastructure.kakao.KakaoUserService.getUserInfo(com.depromeet.todo.domain.member.oauth.OAuthCredential))")
    public Object mockKakaoUser(ProceedingJoinPoint pjp) throws Throwable {
        try {
            OAuthAccessToken oAuthAccessToken = (OAuthAccessToken) pjp.getArgs()[0];
            if (Objects.equals("kakaoAccessToken", oAuthAccessToken.getCredential())) {
                return OAuthUserInfo.fromKakao("providerUserId");
            }
        } catch (Exception ignored) {
            // do nothing
        }
        return pjp.proceed();
    }
}
