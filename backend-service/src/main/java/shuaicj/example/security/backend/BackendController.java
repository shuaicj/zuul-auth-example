package shuaicj.example.security.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller.
 *
 * @author shuaicj 2017/10/18
 */
@RestController
public class BackendController {

    @GetMapping("/admin")
    public String admin(@RequestHeader("X-Username") String username) {
        return "Hello Admin: " + username + "!\n";
    }

    @GetMapping("/user")
    public String user(@RequestHeader("X-Username") String username) {
        return "Hello User: " + username + "!\n";
    }

    @GetMapping("/guest")
    public String guest(@RequestHeader("X-Username") String username) {
        return "Hello Guest: " + username + "!\n";
    }
}

