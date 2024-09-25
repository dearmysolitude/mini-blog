package kr.luciddevlog.saebyukLog.user.service;

import kr.luciddevlog.saebyukLog.user.entity.CustomUserDetails;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import kr.luciddevlog.saebyukLog.user.repository.UserItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    final UserItemRepository userItemRepository;
    @Autowired
    public CustomUserDetailsService(UserItemRepository userItemRepository) {
        this.userItemRepository = userItemRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserItem userItem = userItemRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(userItem);
    }

}
