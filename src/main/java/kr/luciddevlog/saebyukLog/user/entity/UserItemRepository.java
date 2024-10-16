package kr.luciddevlog.saebyukLog.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    Optional<UserItem> findByUsername(String username);
    UserItem findByEmail(String phoneNumber);
    UserItem findByName(String name);
    UserItem findByRefreshToken(String refreshToken);
    UserItem findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    boolean existsByUsername(String username);
}
