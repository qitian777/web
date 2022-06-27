package com.tian.web.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProcessParameter {
    public final String[] types = {"电影", "电视剧", "动漫", "纪录片"};

    public final String[] orders = {"评分", "上映时间"};

    public boolean checkType(String type) {
        return Arrays.asList(types).contains(type);
    }

    public boolean checkOrder(String order) {
        return Arrays.asList(types).contains(order);
    }

    // 获取风格的所有可能组合
    public List<List<String>> getStyleList(List<String> styles) {
        List<List<String>> stylesList = new ArrayList<>();
        stylesList.add(styles);
        int i = 0;
        while (stylesList.get(i).size() != 1) {
            for (int k = 0; k < stylesList.get(i).size(); k++) {
                if (!stylesList.contains(subList(stylesList.get(i), k))) {
                    stylesList.add(subList(stylesList.get(i), k));
                }
            }
            i++;
        }
        stylesList.remove(0);
        return stylesList;
    }

    List<String> subList(List<String> list, int index) {
        List<String> subList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i != index) {
                subList.add(list.get(i));
            }
        }
        return subList;
    }
}
