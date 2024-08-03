package br.com.blz.testjava.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private Long sku;
    private String name;
    private Inventory inventory;
    private Boolean isMarketable;
}
