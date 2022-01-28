package repository;

import entity.User;

public interface UserRepository {
    public Integer addUser(User user);
    public User findUserByEmailAndPassword(User user);
}
