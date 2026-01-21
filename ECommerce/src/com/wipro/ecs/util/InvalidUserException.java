package com.wipro.ecs.util;

public class InvalidUserException extends Exception{
	public String toString()
	{
		return "UserId does not exist";
	}

}
