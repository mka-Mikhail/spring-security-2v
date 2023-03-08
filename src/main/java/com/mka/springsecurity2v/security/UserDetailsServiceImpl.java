package com.mka.springsecurity2v.security;

import com.mka.springsecurity2v.model.User;
import com.mka.springsecurity2v.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//здесь мы находим нужного пользователя по его нику/почте, затем, если найдём егш в БД, мы возвращаем спрингового юзера
//это наша реализация UserDetailsService для поиска юзера по username/email
@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User does`t exist"));
        return SecurityUser.fromUser(user);
    }
}
