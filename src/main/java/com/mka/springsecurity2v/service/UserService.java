package com.mka.springsecurity2v.service;

import com.mka.springsecurity2v.model.Role;
import com.mka.springsecurity2v.model.Status;
import com.mka.springsecurity2v.model.User;
import com.mka.springsecurity2v.model.UserDto;
import com.mka.springsecurity2v.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createNewUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirst_name(userDto.getFirst_name());
        user.setLast_name(userDto.getLast_name());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }
}
