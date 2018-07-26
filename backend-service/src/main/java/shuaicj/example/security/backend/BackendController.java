package shuaicj.example.security.backend;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller.
 *
 * @author shuaicj 2017/10/18
 */
@RestController
public class BackendController {

    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin: " + curUser() + "!\n";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello User: " + curUser() + "!\n";
    }

    @GetMapping("/guest")
    public String guest() {
        return "Hello Guest: " + curUser() + "!\n";
    }

    private String curUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

