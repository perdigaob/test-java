package br.com.blz.testjava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Warehouse {

    private String locality;
    private Integer quantity;
    private WarehouseType type;

}
