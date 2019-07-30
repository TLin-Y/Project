/*
 * COMP6231 A2
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */
package client;

import java.util.Scanner;

import functions.City;
import functions.FuntionMembers;
import functions.Role;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import java.io.*;
import java.util.*;
//Client method for ID input, login, automatically transfer to customer/manager.
public class Client {

	static Scanner input = new Scanner(System.in);


	public static void main(String[] args) {

		System.out.println("Event Booking System Inilailized!");
		System.out.print("Please enter your ID : ");
		String id = input.next();

		User user = new User();
		String value = validateUser(id, user);

		switch (value) {

		case "success":
			System.out.println("Login Successful : " + user);
			Thread t = null;
			if (user.getRole() == Role.Customer) {
				t = new Thread(new Customers(args,user));
			} else {
				t = new Thread(new Managers(args,user));
			}
			//Start thread for UI control.
			t.start();
			break;
		default:
			System.out.println(value);
			break;
		}

	}

//Local check function for ID format, it ensure a right ID transfer to Server.
	private static String validateUser(final String id, final User user) {
		String returnValue = null, city, role, value;
		int userId;
		// ID length !=8
		if (id.length() != 8)
			return "Seems to be an invalid id(length not equal to8).";

		city = id.substring(0, 3);
		role = id.substring(3, 4);
		value = id.substring(4);

		// validate city(should in MTL OTW or TOR)
		if (!FuntionMembers.cityMatch(city))
			return "Your city('" + city + "') isn't recognized.";
		// validate role(C or M)
		else if (!FuntionMembers.roleMatch(role))
			return "Your role('" + role + "') isn't recognized.";

		try {
			// validate user id (integer value)
			userId = Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return "Your id('" + value + "') isn't recognized.";
		}
		returnValue = "success";
		user.setcity(City.valueOf(city.toUpperCase()));
		user.setRole(Role.fromString(role.toUpperCase()));
		user.setId(userId);
		return returnValue;
	}

}
