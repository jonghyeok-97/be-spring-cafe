package codesquad.springcafe.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // userID 를 찾으면 유저 프로필 조회, 못 찾으면 로그인 페이지로 이동.
    @GetMapping("/users/{userId}")
    public String showProfile(@PathVariable String userId, Model model) {
        Optional<User> optUser = userRepository.findUser(userId);
        Optional<UserDTO> optUserDTO = optUser.map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            return userDTO;
        });

        return optUserDTO.map(userDTO -> {
            model.addAttribute("user", userDTO);
            return "user/profile";
        }).orElse("user/login");
    }

    // 회원가입 기능
    @PostMapping("/user")
    public String create(@ModelAttribute UserDto userDto) {
        userRepository.save(userDto);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserDto> userDtos = userRepository.getUserList();
        model.addAttribute("users", userDtos);
        return "user/list";
    }

    @GetMapping("/users/{userId}")
    public String showProfile(@PathVariable String userId, Model model) {
        UserDto userDto = userRepository.findUser(userId);
        model.addAttribute("user", userDto);
        return "user/profile";
    }
}
