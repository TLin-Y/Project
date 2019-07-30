/*
 * COMP6231 A1
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */
package client;

import functions.City;
import functions.Constants;
import functions.Role;

//Using User type to divided information from ID
public class User {

	private City city;
	private Role role;
	private int id;

	public User() {
		
	}
	User(City city, Role role, int id) {
		this.city = city;
		this.role = role;
		this.id = id;
	}
	
	/**
	 * @return the city
	 */
	public City getcity() {
		return city;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param dept the dept to set
	 */
	public void setcity(City city) {
		this.city = city;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	

	public String toString() {
		return city + Constants.EMPTYSTRING + role + Constants.EMPTYSTRING + id;
	}
	
}
