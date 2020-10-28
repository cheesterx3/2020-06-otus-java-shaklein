package ru.otus.controllers;

import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.domain.User;
import ru.otus.dto.UserDto;
import ru.otus.services.DBServiceUser;

import static java.util.stream.Collectors.toList;

@Controller
public class UserController {

    private final DBServiceUser usersService;

    public UserController(@Qualifier("cacheDbUserService") DBServiceUser usersService) {
        this.usersService = usersService;
    }

    @GetMapping({"/", "/user/list"})
    public String userListView(Model model) {
        val users = usersService.getAll();
        val dtoList = users.stream().map(UserDto::fromUser).collect(toList());
        model.addAttribute("users", dtoList);
        return "userList.html";
    }

    @GetMapping("/user/create")
    public String userCreateView(Model model) {
        model.addAttribute("user", new UserDto());
        return "userCreate.html";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute UserDto userDto) {
        usersService.save(User.fromDto(userDto));
        return new RedirectView("/", true);
    }

}
