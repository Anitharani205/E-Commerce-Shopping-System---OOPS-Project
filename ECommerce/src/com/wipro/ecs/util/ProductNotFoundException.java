package com.wipro.ecs.util;

public class ProductNotFoundException extends Exception{
     public String toString()
     {
    	 return "Product does not match with available products";
     }
}
