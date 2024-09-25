package kr.luciddevlog.saebyukLog.user;

import jakarta.persistence.EntityManager;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import kr.luciddevlog.saebyukLog.user.entity.UserRole;
import kr.luciddevlog.saebyukLog.user.exception.UserNotFoundException;
import kr.luciddevlog.saebyukLog.user.repository.UserItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);
    UserItemRepository userItemRepository;
    EntityManager entityManager;
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepositoryTest(UserItemRepository userItemRepository, EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.userItemRepository = userItemRepository;
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @AfterEach
    void after() {
        entityManager.clear();
    }

    private void clear(){
        entityManager.flush();
        entityManager.clear();
    }

    /* 회원 저장
    1. 아이디 빈 칸
    2. 이름 빈 칸
    3. 비밀번호 빈 칸: OAuth2 일때는..? > 다시
    4. 이메일 중복
    5. 아이디 중복
    * */
    @Test
    void saveNewUser() {
        // given, when
        UserItem userItem = UserItem.builder()
                .username("username")
                .password("1234567890")
                .name("Member1")
                .role(UserRole.ROLE_USER)
                .build();
        UserItem saveItem = userItemRepository.save(userItem);
        UserItem findItem = userItemRepository.findByUsername(userItem.getUsername()).orElseThrow(() -> new UserNotFoundException("저장된 회원이 없음"));

        //then
        assertThat(findItem).isSameAs(saveItem);
        assertThat(findItem).isSameAs(userItem);
        entityManager.flush();
    }

    @Test
    void saveNewUser2() {
        // given, when
        UserItem userItem = UserItem.builder()
                .password("1234567890")
                .name("Member1")
                .role(UserRole.ROLE_USER)
                .build();
        // when & then
        assertThrows(Exception.class, () -> userItemRepository.save(userItem));
    }

    @Test
    void saveNewUser3() {
        // given, when
        UserItem userItem = UserItem.builder()
                .username("username")
                .password("1234567890")
                .role(UserRole.ROLE_USER)
                .build();
        // when & then
        assertThrows(Exception.class, () -> userItemRepository.save(userItem));
    }

    @Test
    void saveNewUser_중복username() {
        UserItem userItem = UserItem.builder()
                .username("username")
                .password("1234567890")
                .name("Member1")
                .role(UserRole.ROLE_USER)
                .build();
        userItemRepository.save(userItem);
        clear();
        UserItem userItem2 = UserItem.builder()
                .username("username")
                .password("1234567fads890")
                .name("Member2")
                .role(UserRole.ROLE_USER)
                .build();
        assertThrows(Exception.class, () -> userItemRepository.save(userItem2));
    }

    @Test
    void saveNewUser_중복email() {
        UserItem userItem = UserItem.builder()
                .username("username")
                .password("1234567ee890")
                .name("Member1")
                .email("abc@abc.abc")
                .role(UserRole.ROLE_USER)
                .build();
        userItemRepository.save(userItem);
        clear();
        UserItem userItem2 = UserItem.builder()
                .username("username2")
                .password("1234567fads890")
                .name("Member2")
                .email("abc@abc.abc")
                .role(UserRole.ROLE_USER)
                .build();
        assertThrows(Exception.class, () -> userItemRepository.save(userItem2));
    }

    @Test
    public void 성공_회원수정() throws Exception {
        //given
        UserItem member1 = UserItem.builder().username("username").password("1234567890").name("Member1").role(UserRole.ROLE_USER).build();
        userItemRepository.save(member1);
        clear();

        String updatePassword = "updatePassword";
        String updateName = "updateName";

        //when
        UserItem findMember = userItemRepository.findById(member1.getId()).orElseThrow(() -> new UserNotFoundException("유저 정보를 찾지 못함"));
        findMember.updateName(updateName);
        findMember.updatePassword(updatePassword);
        findMember.encodePassword(passwordEncoder);
        entityManager.flush();

        //then
        UserItem findUpdateMember = userItemRepository.findById(findMember.getId()).orElseThrow(() -> new UserNotFoundException("유저 정보를 찾지 못함"));

        assertThat(findUpdateMember).isSameAs(findMember);
        assertThat(passwordEncoder.matches(updatePassword, findUpdateMember.getPassword())).isTrue();
        assertThat(findUpdateMember.getName()).isEqualTo(updateName);
        assertThat(findUpdateMember.getName()).isNotEqualTo(member1.getName());
    }
    // 회원 삭제
    @Test
    public void 성공_회원삭제() throws Exception {
        //given
        UserItem member1 = UserItem.builder().username("username").password("1234567890").name("Member1").role(UserRole.ROLE_USER).build();
        userItemRepository.save(member1);
        clear();

        //when
        userItemRepository.delete(member1);
        clear();

        //then
        assertThrows(Exception.class,
                () -> userItemRepository.findById(member1.getId())
                        .orElseThrow(() -> new Exception()));
    }


    @Test
    public void existByUsername_정상작동() throws Exception {
        //given
        String username = "username";
        UserItem member1 = UserItem.builder().username("username").password("1234567890").name("Member1").role(UserRole.ROLE_USER).build();
        userItemRepository.save(member1);
        clear();

        //when, then
        assertThat(userItemRepository.existsByUsername(username)).isTrue();
        assertThat(userItemRepository.existsByUsername(username+"123")).isFalse();
    }

    @Test
    public void findByUsername_정상작동() throws Exception {
        //given
        String username = "username";
        UserItem member1 = UserItem.builder().username("username").password("1234567890").name("Member1").role(UserRole.ROLE_USER).build();
        userItemRepository.save(member1);
        clear();


        //when, then
        assertThat(userItemRepository.findByUsername(username).get().getUsername()).isEqualTo(member1.getUsername());
        assertThat(userItemRepository.findByUsername(username).get().getName()).isEqualTo(member1.getName());
        assertThat(userItemRepository.findByUsername(username).get().getId()).isEqualTo(member1.getId());
        assertThrows(Exception.class,
                () -> userItemRepository.findByUsername(username+"123")
                        .orElseThrow(() -> new Exception()));

    }

    @Test
    public void 회원가입시_생성시간_등록() throws Exception {
        //given
        UserItem member1 = UserItem.builder().username("username").password("1234567890").name("Member1").role(UserRole.ROLE_USER).build();
        userItemRepository.save(member1);
        clear();

        //when
        UserItem findMember = userItemRepository.findById(member1.getId()).orElseThrow(() -> new Exception());

        //then
        assertThat(findMember.getCreatedAt()).isNotNull();
        assertThat(findMember.getUpdatedAt()).isNotNull();

    }
}
