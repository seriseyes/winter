package winter;

import winter.annotations.Controller;
import winter.annotations.Get;
import winter.annotations.Param;

@Controller("/user")
public class UserController {

    @Get("/test")
    public String test(@Param("name") String name) {
        return "Hi " + (name == null ? "stranger" : name) + ", It works";
    }
}
