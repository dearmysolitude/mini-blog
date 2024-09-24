package kr.luciddevlog.saebyukLog.user;

import jakarta.persistence.EntityManager;
import kr.luciddevlog.saebyukLog.user.repository.UserItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    UserItemRepository userItemRepository;
    EntityManager entityManager;

    @Autowired
    UserServiceTest(UserItemRepository userItemRepository, EntityManager entityManager) {
        this.userItemRepository = userItemRepository;
        this.entityManager = entityManager;
    }

    @AfterEach
    void after() {
        entityManager.clear();
    }


    /* 회원 저장
    1. 아이디 빈 칸
    2. 이름 빈 칸
    3. 전화번호 빈 칸
    4. 전화번호 중복
    5. 아이디 중복
    * */

    // 회원 수정


    // 회원 삭제

    // 기타
    //username으로 찾기 시 기능이 잘 작동하는지
    //
}
