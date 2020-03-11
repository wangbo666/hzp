package com.qgyyzs.globalcosmetics.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/22.
 */

public class UserBean implements Serializable {
    private String username;
    private String nickname;
    private String photo;

    public UserBean(String username, String nickname, String photo) {
        this.username = username;
        this.nickname = nickname;
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
