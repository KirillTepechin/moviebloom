package moviebloom.controller;

import moviebloom.domain.User;
import moviebloom.dto.LoginDto;
import moviebloom.dto.RegisterDto;
import moviebloom.mapper.UserMapper;
import moviebloom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterDto registerDto) {
        User user = userMapper.fromRegisterDto(registerDto);
        userService.register(user);
    }

    @PostMapping("/auth")
    public String auth(@RequestBody LoginDto dto) {
        return userService.auth(dto.getLogin(), dto.getPassword());
    }

    @GetMapping("/me")
    public User me(@AuthenticationPrincipal User user) {
        return user;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String loginException(NotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String userAlreadyExist(IllegalArgumentException e) {
        return e.getMessage();
    }
}
