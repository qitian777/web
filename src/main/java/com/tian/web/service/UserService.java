package com.tian.web.service;

import com.tian.web.mapper.UserMapper;
import com.tian.web.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int updateUserInfo(Map<String, Object> map) {
        return userMapper.updateUserInfo(map);
    }

    public User getUserByUsername(String name) {
        return userMapper.getUserByUsername(name);
    }

    public int checkUserByUsername(String name) {
        return userMapper.checkUserByUsername(name);
    }

    public boolean registerUser(User user, String table) {
        boolean result = false;
        if (userMapper.saveUser(user) > 0) {
            userMapper.createCollections(table);
            result = true;
        }
        return result;
    }

    public int getTopId() {
        return userMapper.getTopId();
    }
}
