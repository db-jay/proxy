package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.*;

/* @Controller 또는 @RequestMapping 이 있어야 스프링 컨트롤러로 인식함. */
// @RequestMapping
// @ResponseBody

/*
* 스프링 부트 3.0(스프링 프레임워크 6.0)부터는 클래스 레벨에 @RequestMapping 이 있어도 스프링 컨트롤러로 인식하지 않는다.
* 오직 @Controller 가 있어야 스프링 컨트롤러로 인식한다. 참고로 @RestController 는 해당 애노테이션 내부에 @Controller 를 포함하고 있으므로 인식 된다.
* 따라서 '@RestController' annotation을 사용해야 한다.
*/

@RestController
public interface OrderControllerV1 {
    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
