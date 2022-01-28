package service;

import entity.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입시 입력값을 검증하고 db에 저장하는 로직
     * @param user : 들어오는 유저 입력값
     * @return : 가입 성공시 true, 실패시 false 반환
     */
    public boolean createAccount(User user){

        boolean result = true;
        Integer uc = 0;
        uc = userRepository.addUser(user);
        if(uc==0){
            result = false;
        }
        return result;
    }

    /**
     * 로그인시 입력값을 검증하고 로그인된 유저를 User 객체에 담는 로직
     * @param user : 들어오는 유저 입력값(email과 password만 있음)
     * @return : 로그인 성공시db에 있는 user 정보를 user객체에 전부 담아서 줌
     *           실패시 User객체에 null을 담아서 반환
     */
    public User loginAuthorization(User user){
        User result = null;
        result = userRepository.findUserByEmailAndPassword(user);

        return result;
    }


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
