package com.tian.web.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class User implements Serializable, UserDetails {
    private int id;
    private String nickname;
    private String username;
    private String password;
    private String create_time;
    private String picture;
    private List<GrantedAuthority> authorities;

    public User() {
    }

    public User(int id, String nickname, String username, String password, String create_time) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.create_time = create_time;
    }

    public User(int id, String nickname, String username, String password, String create_time, String picture, List<GrantedAuthority> authorities) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.create_time = create_time;
        this.picture = picture;
        this.authorities = authorities;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreateTime(String createTime) {
        this.create_time = createTime;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

//    public void setAuthorities(List<GrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }

    public void setAuthorities(String authorities) {
        this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCreateTime() {
        return create_time;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", create_time='" + create_time + '\'' +
                ", picture='" + picture + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
