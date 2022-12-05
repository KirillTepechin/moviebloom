package moviebloom.service;

import moviebloom.domain.User;
import moviebloom.jwt.JwtProvider;
import moviebloom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.validation.ValidationException;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtProvider jwtProvider;

    public User findOne(Long id){
        return userRepository.findById(id).get();
    }
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void register(User user) {
        if (findByLogin(user.getLogin()) != null) {
            throw new IllegalArgumentException(String.format("Пользователь '%s' уже существует", user.getLogin()));
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String auth(String login, String password) {
        User user = userRepository.findByLogin(login);
        if(user==null){
            throw new NotFoundException("Неправильный логин или пароль");
        }
        if (encoder.matches(password, user.getPassword())) {
            return jwtProvider.generateToken(login);
        }
        else {
            throw new NotFoundException("Неправильный логин или пароль");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }
}
