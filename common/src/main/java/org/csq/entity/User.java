package org.csq.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;

    /**
     * Order和User都存在Lombok不生效的情况，因此无奈增加了set、get方法
     * @return
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
