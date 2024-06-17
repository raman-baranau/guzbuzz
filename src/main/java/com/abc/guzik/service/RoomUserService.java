package com.abc.guzik.service;

import com.abc.guzik.model.User;
import com.abc.guzik.model.dto.RegisteredUserDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface RoomUserService {
    RegisteredUserDto createUser(User user);
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
