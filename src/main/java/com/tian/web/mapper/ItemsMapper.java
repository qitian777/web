package com.tian.web.mapper;

import com.tian.web.pojo.Items;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ItemsMapper {
    List<Items> getNewItems(String type);

    List<Items> getTopItems(String type);

    List<Items> getSortItems(Map<String, Object> map);

    int getItemsCount(Map<String, Object> map);

    Items getDetailItem(int id);

    List<Items> getSimilarItems(Map<String, Object> map);

    List<Items> searchItems(Map<String, Object> map);

    int searchItemsCount(Map<String, Object> map);

    int checkStore(Map<String, Object> map);

    int addCollection(Map<String, Object> map);

    int deleteCollection(Map<String, Object> map);

    int getCollItemsCount(Map<String, Object> map);

    List<Items> getCollItems(Map<String, Object> map);
}
