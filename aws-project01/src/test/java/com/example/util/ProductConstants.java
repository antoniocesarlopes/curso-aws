package com.example.util;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Product;

@SuppressWarnings("serial")
public class ProductConstants {
	public static final Product PRODUCT = new Product(null, "name", "model", "code", 100.0, "color");
	public static final Product PRODUCT_INVALID = new Product(null, "", "", "", null, "");
	public static final Product PRODUCT_EMPTY = new Product();
	public static final Product PRODUCT_NULL = null;
	public static final Product PRODUCT_INEXISTENT = new Product(999L, "none", "none", "none", 0.0, "none");

	public static final Product PRODUCT_01 = new Product(1L, "Motorola", "G60", "60", 1799.99, "black");
	public static final Product PRODUCT_02 = new Product(2L, "Samsung", "A70", "70", 1599.99, "grey");
	public static final Product PRODUCT_03 = new Product(3L, "Iphone", "6", "6", 799.99, "white");
	
	public static final Product PRODUCT_01_UPDATED = new Product(1L, "Motorola", "G62", "62", 1999.99, "black");
	
	public static final List<Product> PRODUCT_LIST = new ArrayList<>() {
		{
			add(PRODUCT_01);
			add(PRODUCT_02);
			add(PRODUCT_03);
		}
	};
}