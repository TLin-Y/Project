/*
 * COMP6231 A1
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */
package functions;

//Constants class for quick modify
public class Constants {
	
	public static final String LOG_MSG = "METHOD[%s]; PARAMETERS%s; STATUS[%s]; SERVER_MESSAGE[%s]";
	public static final String OP_ADD_EVENT = "addEvent";
	public static final String OP_REMOVE_EVENT = "removeEvent";
	public static final String OP_LIST_EVENT_AVAILABILITY = "listEventAvailability";
	public static final String OP_BOOK_EVENT = "bookEvent";
	public static final String OP_GET_EVENT_SCHEDULE = "getEventSchedule";
	public static final String OP_DROP_EVENT = "dropEvent";
	public static int MAX_EVENT_TAKEN_BY_CUSTOMER = 3;
	public static int MAX_OUTCITY_EVENTS = 3;
	public static final String UNDERSCORE = "_";
	public static final String EMPTYSTRING = "";
	public static final String CAPACITY = "capacity";
	public static final String CUSTOMER_ENROLLED = "customersEnrolled";
	public static final String CUSTOMER_IDS = "customerIds";
	public static final String CUSTOMER_ID = "customerId";
	public static final String EVENT_ID = "eventId";
	public static final String EVENTTYPE = "eventtype";
	public static final String CITY = "city";
	public static final String OP_SWAP_EVENT = "swapEvent";
	public static final String MANAGER_LOG_DIRECTORY = "./src/main/resources/logs/manager/";
	public static final String CUSTOMER_LOG_DIRECTORY = "./src/main/resources/logs/customer/";
	public static final String SERVER_LOG_DIRECTORY = "./src/main/resources/logs/server/";
	public static final String NEW_EVENT_ID = "neweventId";
	public static final String OLD_EVENT_ID = "oldeventId";
	public static final String OLD_EVENT_CITY="oldeventcity";
}
