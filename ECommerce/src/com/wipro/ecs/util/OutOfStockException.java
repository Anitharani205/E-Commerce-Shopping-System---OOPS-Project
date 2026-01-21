package com.wipro.ecs.util;

public class OutOfStockException extends Exception{
   public String toString()
   {
	   return "Users attempt to purchase product with insufficient stock";
   }
}
