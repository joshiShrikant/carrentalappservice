package com.example.carrental.dao;
import com.example.carrental.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private final RedisTemplate<String, User> redisTemplate;

    private static final String USER_KEY = "USER_DATA";

    public UserDao(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public User saveUser(User user) {
        redisTemplate.opsForHash().put(USER_KEY, user.getId().toString(), user);
        return user;
    }

    public User getUser(Long id) {
        return (User) redisTemplate.opsForHash().get(USER_KEY, id.toString());
    }

    public List<User> getAllUsers() {
        return redisTemplate.opsForHash()
                .values(USER_KEY)
                .stream()
                .map(obj -> (User) obj)  // should now deserialize as User
                .toList();
    }

    public void deleteUser(Long id) {
        redisTemplate.opsForHash().delete(USER_KEY, id.toString());
    }
}