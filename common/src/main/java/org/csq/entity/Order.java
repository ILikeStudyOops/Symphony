package org.csq.entity;

import lombok.Data;

@Data
public class Order {
    private Integer id;
    private Double price;
    private User user;
}
