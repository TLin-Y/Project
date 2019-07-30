/*
 * COMP6231 A2
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */
package functions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap.SimpleEntry;

//Functions are designed here
public class FuntionMembers {

	/**
	 * Validates a User
	 * 
	 * @param id
	 * @param userRole
	 * @param city
	 * @return
	 */
	public static SimpleEntry<Boolean, String> validateUser(final String id, final Role userRole,
			final City city) {
		String ci, role, value;
		// string length !=8
		if (id.length() != 8)
			return new SimpleEntry<Boolean, String>(false, "Invalid id(length not equal to 8).");

		ci = id.substring(0, 3).toUpperCase();
		role = id.substring(3, 4).toUpperCase();
		value = id.substring(4);

		// validate city
		if (!cityMatch(ci))
			return new SimpleEntry<Boolean, String>(false, "The City('" + ci + "') isn't recognized.");
		else if (city != null && city != City.valueOf(ci))
			return new SimpleEntry<Boolean, String>(false,
					"You are not authorized for this City('" + ci + "').");
		else if (!roleMatch(role))
			return new SimpleEntry<Boolean, String>(false, "The role('" + role + "') isn't correct.");
		else if(role!=null && userRole!=Role.fromString(role)) {
			return new SimpleEntry<Boolean, String>(false, "This operation is invalid for an advisor('" + role + "').");
		}

		try {
			Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return new SimpleEntry<Boolean, String>(false, "The id('" + value + "') isn't correct.");
		}

		return new SimpleEntry<Boolean, String>(true, "valid");
	}

	/**
	 * Validates a event
	 * 
	 * @param eventId
	 * @return
	 */
	public static SimpleEntry<Boolean, String> validateEvent(final String evenId) {
		return validateEvent(evenId,null,null);

	}
	public static SimpleEntry<Boolean, String> validateEvent(final String evenId, City city , String time) {

		if (evenId.length() != 10)
			return new SimpleEntry<Boolean, String>(false, "Invalid event(length not equal to 10).");
		
		String ci, ti,value;
		int date,month,year;
		
		ci = evenId.substring(0, 3).toUpperCase();
		ti = evenId.substring(3,4).toUpperCase();
		value = evenId.substring(6);
		date = Integer.parseInt(evenId.substring(4,6));
		month = Integer.parseInt(evenId.substring(6,8));
		year = Integer.parseInt(evenId.substring(8,10));
		
		if (!ti.matches("A|M|E"))
			return new SimpleEntry<Boolean, String>(false, "Invalid time slots [4th char not equal to Morning(M), "
					+ "Afternoon(A) and Evening(E)].");
		if (date<1||date>31)
			return new SimpleEntry<Boolean, String>(false, "Invalid date (Should be 1-31)");
		if (month<1||month>12)
			return new SimpleEntry<Boolean, String>(false, "Invalid month (Should be 1-12)");
		if (year<19)
			return new SimpleEntry<Boolean, String>(false, "Invalid year (Should be 2019 or more)");
		if (!FuntionMembers.cityMatch(ci))
			return new SimpleEntry<Boolean, String>(false, "The city('" + ci + "') isn't recognized.");
		else if (city != null && city != City.valueOf(ci))
			return new SimpleEntry<Boolean, String>(false,
					"You are not authorized for this city('" + ci + "').");
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return new SimpleEntry<Boolean, String>(false, "Event id('" + value + "') isn't valid.");
		}
		//System.out.println("Event format validated!");
		//System.out.println("[City]: "+ci+" [Time slots]: "+ti+" [Date]: "+date+" [Month]: "+month+" [Year]: "+year);
//MTLE130519
		return new SimpleEntry<Boolean, String>(true, "valid");
	}

	/**
	 * Validates a eventType
	 * @param eventType
	 * @return
	 */
	public static SimpleEntry<Boolean, String> validateType(String eventType) {
		boolean status = EventType.isValidEventType(eventType);
		String et = null;
		if (!status)
			et = eventType + " isn't valid Type.";
		return new SimpleEntry<Boolean, String>(status, et);
	}

	/**
	 * Converts from object to byte array
	 * @param obj
	 * @return
	 */
	public static byte[] objectToByteArray(Object obj) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteOut.toByteArray();
	}

	/**
	 * converts from byte array to object
	 * @param data
	 * @return
	 */
	public static Object byteArrayToObject(byte[] data) {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		Object result = null;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(byteIn);
			result = (Object) in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static boolean cityMatch(final String city) {
		return city.matches("TOR|MTL|OTW");
	}

	public static boolean roleMatch(final String role) {
		return role.matches("(?i)C|M");
	}
}
