package com.tian.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WebApplicationTests {


    @Test
    void test01(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("qi123456"));
    }

    @Test
    void test02(){
        String time= String.valueOf(System.currentTimeMillis());
        int length = time.length();
        String code=time.substring(length-6);
        System.out.println(time);
        System.out.println(code);
    }

    @Test
    void contextLoads() {
        String s = "人文/探险/自然/旅行";
        String[] split = s.split("/");
        List<List<String>> stylesList = new ArrayList<>();
        List<String> styles = new ArrayList<>(Arrays.asList(split));
        stylesList.add(styles);
        int i = 0;
        while (true) {
            if (stylesList.get(i).size() == 1) {
                break;
            }
            for (int k = 0; k < stylesList.get(i).size(); k++) {
                if (!stylesList.contains(subList(stylesList.get(i),k))){
                    stylesList.add(subList(stylesList.get(i),k));
                }
            }
            i++;
        }
        stylesList.remove(0);
        System.out.println(stylesList);
//        for (int i = 0; i <split.length; i++) {
//            stylesList.add(subList(styles,i));
//        }

    }

    List<String> plusList(List<String> list, String plus) {
        List<String> plusList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            plusList.add(list.get(i));
        }
        plusList.add(plus);
        return plusList;
    }

    List<String> subList(List<String> list, int index) {
        List<String> plusList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i != index) {
                plusList.add(list.get(i));
            }
        }
        return plusList;
    }

}
