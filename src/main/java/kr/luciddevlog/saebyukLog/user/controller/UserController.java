package kr.luciddevlog.saebyukLog.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.luciddevlog.saebyukLog.user.dto.RegisterFormDto;
import kr.luciddevlog.saebyukLog.user.exception.UserAlreadyExistsException;
import kr.luciddevlog.saebyukLog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String toLoginPage() {
        return "member/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String makeNewUser(@Valid @ModelAttribute RegisterFormDto form, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
            return "member/signup";
        }

        List<String> messages = new ArrayList<>();
        try {
            userService.register(form);
            messages.add("회원 가입 완료: 로그인 하세요.");
            model.addAttribute("messages", messages);
            return "member/login";
        } catch (UserAlreadyExistsException e) {
            messages.add(e.getMessage());
            model.addAttribute("messages", messages);
            return "member/signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
