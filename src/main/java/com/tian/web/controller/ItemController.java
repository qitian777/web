package com.tian.web.controller;

import com.tian.web.pojo.Items;
import com.tian.web.service.ItemsService;
import com.tian.web.util.ProcessParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ItemController {
    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ProcessParameter processParameter;

    @RequestMapping("/devLog")
    public String getDevLog(){
        return "dev_log";
    }

    //主页
    @RequestMapping(value = {"/", "/index"})
    public String getMovies(Model model) {
        model.addAttribute("type","");
        List<Items> movies = itemsService.getNewMovies();
        model.addAttribute("movies", movies);
        List<Items> topMovies = itemsService.getTopMovies();
        model.addAttribute("topMovies", topMovies);
        List<Items> dramas = itemsService.getNewDramas();
        model.addAttribute("dramas", dramas);
        List<Items> topDramas = itemsService.getTopDramas();
        model.addAttribute("topDramas", topDramas);
        List<Items> animations = itemsService.getNewAnimations();
        model.addAttribute("animations", animations);
        List<Items> topAnimations = itemsService.getTopAnimations();
        model.addAttribute("topAnimations", topAnimations);
        return "index";
    }

    //list页面
    @RequestMapping("/list")
    public String getList(@RequestParam String type, String area, String style, String year,
                          @RequestParam(required = false, defaultValue = "上映时间") String order,
                          @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        String htmlPage = "error/4xx";
        if (!processParameter.checkType(type)) {
            model.addAttribute("status", "parameter error");
            model.addAttribute("error", "参数输入错误！");
            return htmlPage;
        }
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("type", type);
        model.addAttribute("type", type);
        if (area != null && !"".equals(area)) {
            parameter.put("area", area);
            model.addAttribute("area", area);
        }
        if (style != null && !"".equals(style)) {
            parameter.put("style", style);
            model.addAttribute("style", style);
        }
        if (year != null && !"".equals(year)) {
            if ("更早".equals(year)){
                parameter.put("year", 2009);
            }else{
                parameter.put("year", Integer.parseInt(year));
            }
            model.addAttribute("year", year);
        }
        if (order != null && !"".equals(order)) {
            if (order.equals("上映时间")) {
                parameter.put("order", "show_time");
            } else if (order.equals("评分")) {
                parameter.put("order", "bi_score");
            }
            model.addAttribute("order", order);
        }
        // 符合条件的数据总数
        int itemsCount = itemsService.getItemsCount(parameter);
        // 总分页数
        int totalPage = itemsCount % 12 == 0 ? itemsCount / 12 : itemsCount / 12 + 1;
        if (totalPage==0){
            model.addAttribute("sortItems",null);
            totalPage=1;
            page=1;
        }else{
            if (page > 0 && page <= totalPage) {
                parameter.put("start", (page - 1) * 12);
            }
            // 符合条件的数据
            List<Items> sortItems = itemsService.getSortItems(parameter);
            model.addAttribute("sortItems", sortItems);
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("go_page", page);
        switch (type) {
            case "电影":
                htmlPage = "movie_list";
                break;
            case "电视剧":
                htmlPage = "drama_list";
                break;
            case "动漫":
                htmlPage = "animation_list";
        }
        return htmlPage;
    }

    //详情页
    @RequestMapping("/detail")
    public String getDetail(@RequestParam int id, Model model) {
        Items detailItem = itemsService.getDetailItem(id);
        if (detailItem == null) {
            model.addAttribute("status", "id error");
            model.addAttribute("error", "请求的内容不存在！");
            return "error/4xx";
        }
        model.addAttribute("detailItem", detailItem);
        // 获取推荐
        Map<String, Object> similar = new HashMap<>();
        String type = "";
        String area = "";
        String[] styles = null;
        if (detailItem.getType().contains("/")) {
            String[] types = detailItem.getType().split("/");
            type = detailItem.getType() + "|" + types[0] + "|" + types[1];
        } else {
            type = detailItem.getType();
        }
        if (detailItem.getArea().contains("/")) {
            String[] areas = detailItem.getType().split("/");
            area = detailItem.getArea();
            for (String ar : areas) {
                area = area + "|" + ar;
            }
        } else {
            area = detailItem.getArea();
        }
        if (detailItem.getStyle().contains("/")) {
            styles = detailItem.getStyle().split("/");
        } else {
            styles = new String[]{detailItem.getStyle()};
        }
        List<Integer> ids = new ArrayList<>();
        ids.add(detailItem.getId());
        similar.put("type", type);
        similar.put("area", area);
        similar.put("styles", styles);
        similar.put("ids", ids);
        similar.put("size", 6);
        List<Items> similarItems = itemsService.getSimilarItems(similar);
        int size = similarItems.size();
        // 若相同类型、地方、风格的不够6个，增加不同地方的
        if (size < 6) {
            similar.remove("area");
            similar.put("size", 6 - size);
            similarItems.addAll(itemsService.getSimilarItems(similar));
            size = similarItems.size();
        }
        // 若加上不同地方的仍不够六个，风格数减一
        if (size < 6) {
            if (styles.length > 1) {
                List<List<String>> styleList = processParameter.getStyleList(Arrays.asList(styles));
                int i = 0;
                while (size < 6 || i == styleList.size()) {
                    similar.put("styles", styleList.get(i));
                    similar.put("size", 6 - size);
                    similarItems.addAll(itemsService.getSimilarItems(similar));
                    size = similarItems.size();
                    i++;
                }
            }
        }
        model.addAttribute("similarItems", similarItems);
        return "detail";
    }

    @RequestMapping("/search")
    public String searchItems(@RequestParam String string,
                              @RequestParam(required = false, defaultValue = "1") int page,
                              Model model) {
        if (string==null || "".equals(string)){
            return "redirect:/";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("string", string);
        int itemsCount = itemsService.searchItemsCount(map);
        int totalPage = itemsCount % 12 == 0 ? itemsCount / 12 : itemsCount / 12 + 1;
        model.addAttribute("go_page", page);
        if (totalPage==0){
            model.addAttribute("totalPage", 1);
            return "search";
        }
        model.addAttribute("totalPage", totalPage);
        if (page > totalPage || page < 1) {
            model.addAttribute("status", "parameter error");
            model.addAttribute("error", "参数输入错误！");
            return "error/4xx";
        }
        int start = 12 * (page - 1);
        map.put("start", start);
        List<Items> searchItems = itemsService.searchItems(map);
        model.addAttribute("searchItems",searchItems);
        model.addAttribute("search",string);
        return "search";
    }

}
