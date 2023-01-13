package org.csq.entity;

import lombok.Data;

@Data
public class Order {
    private Integer id;
    private Double price;
    private User user;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
