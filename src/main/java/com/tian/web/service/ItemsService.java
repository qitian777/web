package com.tian.web.service;

import com.tian.web.mapper.ItemsMapper;
import com.tian.web.pojo.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemsService {

    @Autowired
    private ItemsMapper itemsMapper;


    public Items getDetailItem(int id) {
        return itemsMapper.getDetailItem(id);
    }

    public List<Items> getSimilarItems(Map<String, Object> map) {
        return itemsMapper.getSimilarItems(map);
    }

    @Cacheable(cacheNames = "newMovies")
    public List<Items> getNewMovies() {
        return itemsMapper.getNewItems("电影");
    }

    @Cacheable(cacheNames = "topMovies")
    public List<Items> getTopMovies() {
        return itemsMapper.getTopItems("电影");
    }

    @Cacheable(cacheNames = "newDramas")
    public List<Items> getNewDramas() {
        return itemsMapper.getNewItems("电视剧");
    }

    @Cacheable(cacheNames = "topDramas")
    public List<Items> getTopDramas() {
        return itemsMapper.getTopItems("电视剧");
    }

    @Cacheable(cacheNames = "newAnimations")
    public List<Items> getNewAnimations() {
        return itemsMapper.getNewItems("动漫");
    }

    @Cacheable(cacheNames = "topAnimations")
    public List<Items> getTopAnimations() {
        return itemsMapper.getTopItems("动漫");
    }

    public List<Items> getSortItems(Map<String, Object> map) {
        return itemsMapper.getSortItems(map);
    }

    public int getItemsCount(Map<String, Object> map) {
        return itemsMapper.getItemsCount(map);
    }

    public List<Items> searchItems(Map<String, Object> map){
        return itemsMapper.searchItems(map);
    }

    public int searchItemsCount(Map<String, Object> map){
        return itemsMapper.searchItemsCount(map);
    }

    public int checkStore(Map<String,Object> map){
        return itemsMapper.checkStore(map);
    }

    public int addCollection(Map<String, Object> map){
        return itemsMapper.addCollection(map);
    }

    public int deleteCollection(Map<String, Object> map){
        return itemsMapper.deleteCollection(map);
    }

    public int getCollItemsCount(Map<String, Object> map){
        return itemsMapper.getCollItemsCount(map);
    }

    public List<Items> getCollItems(Map<String, Object> map){
        return itemsMapper.getCollItems(map);
    }
}
