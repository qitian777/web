package com.tian.web.util;

import com.tian.web.pojo.User;
import com.tian.web.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserParameter {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AsyncService asyncService;

    public boolean checkName(String name) {
        if (name.length() < 3 || name.length() > 9) {
            return false;
        }
        return true;
    }

    public String checkEmail(String email,int code) {
        String error="";
        String check = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        if (!isMatched) {
            error =  "邮箱格式不对。\n";
        } else {
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(email))) {
                error = error +  "邮箱未进行验证。\n";
            } else {
                Object codeString = redisTemplate.opsForValue().get(email);
                int codeRedis = 0;
                if (codeString != null) {
                    codeRedis = Integer.parseInt((String) codeString);
                }
                if (code != codeRedis) {
                    error = error +  "验证码不对。\n";
                }
            }
        }
        return error;
    }

    public boolean checkPw(String password) {
        String pwCheck = "^[A-z][\\w]{6,14}$";
        Pattern pwRegex = Pattern.compile(pwCheck);
        Matcher pwMatcher = pwRegex.matcher(password);
        return pwMatcher.matches();
    }

    public void getCode(String email) throws MessagingException {
        String time = String.valueOf(System.currentTimeMillis());
        int length = time.length();
        String code = time.substring(length - 6);
        //验证码存入redis，并设置十五分钟有效期
        redisTemplate.opsForValue().set(email, code, 900, TimeUnit.SECONDS);
        asyncService.sendEmailCode(email, code);
    }

    public Map<String,Object> getUserTable(Authentication authentication){
        User user= (User) authentication.getPrincipal();
        int userId = user.getId();
        String table="collections.user_"+userId;
        Map<String,Object> map=new HashMap<>();
        map.put("table",table);
        return map;
    }

    public Map<String,Object> getStoreParam(Authentication authentication,int id){
        Map<String, Object> map = getUserTable(authentication);
        map.put("id",id);
        return map;
    }
}
