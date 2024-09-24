package kr.luciddevlog.saebyukLog.user.service;

import kr.luciddevlog.saebyukLog.user.dto.MemberInfoDto;
import kr.luciddevlog.saebyukLog.user.dto.RegisterFormDto;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import kr.luciddevlog.saebyukLog.user.entity.UserRole;
import kr.luciddevlog.saebyukLog.user.exception.UserAlreadyExistsException;
import kr.luciddevlog.saebyukLog.user.repository.UserItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    UserItemRepository userItemRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserItemRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.userItemRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean registered(String userName) {
        return userItemRepository.findByUsername(userName) != null;
    }

    private boolean duplicatedEmail(String email) {
        return userItemRepository.findByEmail(email) != null;
    }

    private void verifyDuplication(RegisterFormDto form) {
        if(registered(form.getUsername())) {
            throw new UserAlreadyExistsException("이미 존재하는 ID 입니다.");
        }
        if(duplicatedEmail(form.getEmail())) {
            throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
        }
    }

    private boolean checkForm(RegisterFormDto form) {
        try {
            verifyDuplication(form);
        } catch (UserAlreadyExistsException uae) {
            log.error(uae.getMessage());
            return true;
        }
        return false;
    }

    public void register(RegisterFormDto form) {
        if (checkForm(form)) return;

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        UserItem register = UserItem.builder()
                .role(UserRole.ROLE_USER)
                .email(form.getEmail())
                .username(form.getUsername())
                .name(form.getName())
                .password(encodedPassword)  // 직접 인코딩된 비밀번호 설정
                .build();

        userItemRepository.save(register);
    }



    public void deleteUser(MemberInfoDto userInfo) {

    }

    // 변경 가능: 비밀 번호, email, name
    public void updateUserInfo(RegisterFormDto form, UserItem currentUserItem) {
        UserItem newUserItem = UserItem.builder()
                .id(currentUserItem.getId())
                .build();

        if(!currentUserItem.getEmail().equals(form.getEmail())) {
            if (checkEmail(form)) return;
            newUserItem.updateEmail(form.getEmail());
        }

        newUserItem.updateName(form.getName());
        newUserItem.updatePassword(passwordEncoder.encode(form.getPassword()));

        userItemRepository.save(newUserItem);
    }

    private boolean checkEmail(RegisterFormDto form) {
        try {
            if(duplicatedEmail(form.getEmail())) {
                throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
            }
        } catch (UserAlreadyExistsException uae) {
            log.error(uae.getMessage());
            return true;
        }
        return false;
    }

}
