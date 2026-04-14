package ru.cdek.TaskTimeTracker.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.cdek.TaskTimeTracker.entity.User;
import ru.cdek.TaskTimeTracker.repository.UserRepository;
import ru.cdek.TaskTimeTracker.security.UserPrincipal;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private static final String ROLE_USER = "ROLE_USER";

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return new UserPrincipal(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority(ROLE_USER)));
  }

  public UserDetails loadUserById(UUID userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

    return new UserPrincipal(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority(ROLE_USER)));
  }
}
