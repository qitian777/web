package com.tian.web.controller;

import com.tian.web.pojo.Items;
import com.tian.web.pojo.User;
import com.tian.web.service.ItemsService;
import com.tian.web.service.UserService;
import com.tian.web.util.ProcessParameter;
import com.tian.web.util.UserParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserParameter userParameter;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private ProcessParameter processParameter;


    @PostMapping("/register")
    public String register(@RequestParam String nickname, @RequestParam String username,
                           @RequestParam int code, @RequestParam String password, Model model,
                           RedirectAttributes redirectAttributes) {
        String error = "";
        // 检查邮箱
        if (!"".equals(userParameter.checkEmail(username, code))) {
            error = error + userParameter.checkEmail(username, code);
        }
        //确认邮箱未被注册
        if (userService.checkUserByUsername(username) > 0) {
            error = "邮箱已被注册！";
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/signUp";
        }
        // 检查用户名
        if (!userParameter.checkName(nickname)) {
            error = error + "用户名过长或过短。\n";
        }
        //检查密码
        if (!userParameter.checkPw(password)) {
            error = error + "密码不符合要求。\n";
        }
        if ("".equals(error)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String createDate = formatter.format(date);
            int id = userService.getTopId() + 1;
            User user = new User(id, nickname, username, passwordEncoder.encode(password), createDate);
            String table = "collections.user_" + id;
            boolean result = userService.registerUser(user, table);
            if (result) {
                return "redirect:/logon";
            } else {
                model.addAttribute("error", "创建失败");
                model.addAttribute("status", "Creation failed");
                return "/error/4xx";
            }
        } else {
            error = "注册失败，存在以下问题：\n" + error;
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/signUp";
        }
    }


    @ResponseBody
    @RequestMapping("/getCode")
    public String getCode(@RequestParam String email) throws MessagingException {
        if (userService.checkUserByUsername(email) > 0) {
            return "0";
        }
        userParameter.getCode(email);
        return "1";
    }

    @RequestMapping("/signUp")
    public String signUp(@ModelAttribute String error,
                         Model model) {
        if (!"".equals(error) && error != null) {
            model.addAttribute("error", error);
        }
        return "sign_up";
    }

    @RequestMapping("/logon")
    public String login() {
        return "login";
    }

    @RequestMapping("/user/info")
    public String getUserInfo() {
        return "update_user";
    }

    @RequestMapping("/user/updateInfo")
    public String updateUserInfo(@RequestParam(required = false, defaultValue = "nn") String nickname,
                                 @RequestParam(required = false, defaultValue = "pw") String password,
                                 Principal principal, Model model) {
        Map<String, String> param = new HashMap<>();
        if (!"nn".equals(nickname)) {
            if (userParameter.checkName(nickname)) {
                param.put("nickname", nickname);
            }
        }
        if (!"pw".equals(password)) {
            if (userParameter.checkPw(password)) {
                param.put("password", passwordEncoder.encode(password));
            }
        }
        if (param.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("param", param);
            String username = principal.getName();
            map.put("username", username);
            int i = userService.updateUserInfo(map);
            if (i == 1) {
                User user = userService.getUserByUsername(username);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
            } else {
                model.addAttribute("error", "用户信息修改失败");
            }
        }
        return "update_user";
    }


    @RequestMapping("/resetPage")
    public String getResetPage() {
        return "forget_pw";
    }

    @RequestMapping("/resetPw")
    public String resetPw(@RequestParam String username, @RequestParam int code,
                          @RequestParam String password, Model model) {
        String error;
        if ("".equals(userParameter.checkEmail(username, code))) {
            Map<String, String> param = new HashMap<>();
            param.put("password", passwordEncoder.encode(password));
            Map<String, Object> map = new HashMap<>();
            map.put("param", param);
            map.put("username", username);
            int i = userService.updateUserInfo(map);
            if (i != 1) {
                error = "因未知原因密码修改失败！";
                model.addAttribute("error", error);
            } else {
                return "redirect:/logon";
            }
        } else {
            error = "邮箱未被验证！";
            model.addAttribute("error", error);
        }
        return "redirect:/resetPw";
    }

    @ResponseBody
    @RequestMapping("/getResetCode")
    public String getResetCode(@RequestParam String email) throws MessagingException {
        if (userService.checkUserByUsername(email) > 0) {
            userParameter.getCode(email);
            return "1";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping("/user/checkStore")
    public String checkStore(@RequestParam int id, Authentication authentication) {
        Map<String, Object> map = userParameter.getStoreParam(authentication, id);
        if (itemsService.checkStore(map) > 0) {
            return "1";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping("/user/storeUp")
    public String storeUp(@RequestParam int id, @RequestParam boolean store,
                          Authentication authentication) {
        Map<String, Object> map = userParameter.getStoreParam(authentication, id);
        int i = 0;
        if (store) {
            i = itemsService.addCollection(map);
            if (i == 1) {
                return "a1";
            } else {
                return "a0";
            }
        } else {
            i = itemsService.deleteCollection(map);
            if (i == 1) {
                return "d1";
            } else {
                return "d0";
            }
        }
    }

    @RequestMapping("/user/coll")
    public String getColl(@RequestParam(required = false, defaultValue = "电影") String type,
                          @RequestParam(required = false, defaultValue = "上映时间") String order,
                          @RequestParam(required = false, defaultValue = "1") int page,
                          Authentication authentication, Model model) {
        String htmlPage = "error/4xx";
        if (!processParameter.checkType(type)) {
            model.addAttribute("status", "parameter error");
            model.addAttribute("error", "参数输入错误！");
            return htmlPage;
        }
        Map<String, Object> parameter = userParameter.getUserTable(authentication);
        parameter.put("type", type);
        model.addAttribute("type", type);

        // 符合条件的数据总数
        int itemsCount = itemsService.getCollItemsCount(parameter);
        // 总分页数
        int totalPage = itemsCount % 12 == 0 ? itemsCount / 12 : itemsCount / 12 + 1;
        if (totalPage == 0) {
            model.addAttribute("sortItems", null);
            totalPage = 1;
            page = 1;
            model.addAttribute("totalPage", totalPage);
            model.addAttribute("go_page", page);
            return "collection";
        }
        if (page>0 && page<=totalPage){
            parameter.put("start", (page - 1) * 12);
        }else{
            model.addAttribute("status", "parameter error");
            model.addAttribute("error", "参数输入错误！");
            return htmlPage;
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("go_page", page);

        if (order.equals("上映时间")) {
            parameter.put("order", "show_time");
        } else if (order.equals("更新时间")) {
            parameter.put("order", "update_time");
        }
        model.addAttribute("order", order);

        // 符合条件的数据
        List<Items> sortItems = itemsService.getCollItems(parameter);
        model.addAttribute("sortItems", sortItems);
        model.addAttribute("logged", "yes");
        model.addAttribute("coll", "yes");
        return "collection";
    }
}
