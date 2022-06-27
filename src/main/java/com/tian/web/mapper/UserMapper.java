package com.tian.web.mapper;

import com.tian.web.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    int saveUser(User user);

    int updateUserInfo(Map<String, Object> map);

    User getUserByUsername(String name);

    int checkUserByUsername(String name);

    int createCollections(String table);

    int getTopId();
}
