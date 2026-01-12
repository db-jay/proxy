package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    // 기존에 보았던 코드들과 다르게 target 클래스의 정보가 보이지 않는다.
    // target 클래스의 정보는 MethodInvocation invocation 안에 모두 포함되어 있다
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("Time Proxy 실행");
        long startTime = System.currentTimeMillis();


        // invocation.proceed() 를 호출하면 target 클래스를 호출하고 그 결과를 받는다.
        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("Time Proxy 종료 resultTime={}", resultTime);

        return result;
    }
}
