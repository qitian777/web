package com.tian.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Items implements Serializable {
    private int id;
    private String name;
    private String origin_name;
    private String type;
    private String area;
    private String show_time;
    private int year;
    private int season;
    private String style;
    private int finish;
    private int number;
    private String star;
    private String staff;
    private String introduction;
    private String poster;
    private String alias;
    private String update_time;
    private String bi_url;
    private double bi_score;
}
