package br.com.blz.testjava.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Warehouse {

    private String locality;
    private Integer quantity;
    private WarehouseType type;

}
