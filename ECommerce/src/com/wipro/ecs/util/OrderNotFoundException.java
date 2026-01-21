package com.wipro.ecs.util;

public class OrderNotFoundException extends Exception{
   public String toString()
   {
	   return "Cancel Order does not exist";
   }
}
