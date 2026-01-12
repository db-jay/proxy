package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    /*
    * LogTraceBasicHandler 는 InvocationHandler 인터페이스를 구현해서 JDK 동적 프록시에서 사용된다.
        * private final Object target : 프록시가 호출할 대상이다.
    * String message = method.getDeclaringClass().getSimpleName() + "." ...
        * LogTrace 에 사용할 메시지이다. 프록시를 직접 개발할 때는 "OrderController.request()" 와 같이 프록시마다 호출되는 클래스와 메서드 이름을 직접 남겼다.
        * 이제는 Method 를 통해서 호출되는 메서드 정보와 클래스 정보를 동적으로 확인할 수 있기 때문에 이 정보를 사용하면 된다.
    */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메서드 이름 필터
        String methodName = method.getName(); // save, request, reque*, *est ...

        // 패턴 매치
        // 스프링이 제공하는 PatternMatchUtils.simpleMatch(..) 를 사용하면 단순한 매칭 로직을 쉽게 적용할 수 있다
        if(!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }

        TraceStatus status = null;

        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName();
            status = logTrace.begin(message);

            // logic 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
