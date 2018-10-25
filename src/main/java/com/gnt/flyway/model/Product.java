package com.gnt.flyway.model;

import javax.persistence.Entity;

/**
 * @author quyen.nd
 * @since 10/22/2018
 */
@Entity
public class Product {

    private Long id;

    private String productName;

    private Long price;
}
