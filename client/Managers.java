/*
 * COMP6231 A2
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */
package client;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import functions.Constants;
import functions.EventType;
import functions.FuntionMembers;
import functions.Role;
import logTool.allLogger;
import remoteObject.EventSystemInterface;
import remoteObject.EventSystemInterfaceHelper;

/**
 * UI operation for Manager, runnable.
 * @author TLIN
 *
 */
public class Managers implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	User user;
	Scanner input;
	EventSystemInterface stub;
	String[] args;
	/**
	 * Ctor
	 * @param user <code>User</code> class object
	 */
	public Managers(String[] args,User user) {
		this.user = user;
		this.args = args;
		input = new Scanner(System.in);
	}


	public void run() {

		try {
			setupLogging();
			LOGGER.info("MANAGER LOGIN("+user+")");
			ORB orb = ORB.init(args, null);
			Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			stub = (EventSystemInterface) EventSystemInterfaceHelper.narrow(ncRef.resolve_str(user.getcity().toString()));
			Options();
		} catch (RemoteException e) {
			LOGGER.severe("RemoteException Exception : "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.severe("IO Exception : "+e.getMessage());
			e.printStackTrace();
		} catch (InvalidName e) {
			LOGGER.severe("InvalidName : "+e.getMessage());
			e.printStackTrace();
		} catch (NotFound e) {
			LOGGER.severe("NotFound : "+e.getMessage());
			e.printStackTrace();
		} catch (CannotProceed e) {
			LOGGER.severe("CannotProceed : "+e.getMessage());
			e.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			LOGGER.severe("org.omg.CosNaming.NamingContextPackage.InvalidName : "+e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * This method call options
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void Options() throws RemoteException {

		int userSelection = displayMenu();
		String customerId, eventId, type = null;
		EventType eventtype;
		int eventCapacity = 0;
		SimpleEntry<Boolean, String> result;
		HashMap<String, Integer> eventMap;
		HashMap<String, ArrayList<String>> eventList;
		boolean status;
		Any any;
		
		/* Executes the loop until the managers quits the application i.e. presses 8
		 * 
		 */
		while (userSelection != 8) {
			
			switch (userSelection) {
			case 1:
				System.out.print("Enter the event ID : ");
				eventId = input.next().toUpperCase();
				result = FuntionMembers.validateEvent(eventId.trim(), this.user.getcity(),null);
				int typen;
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}

				System.out.print("Event Capacity : ");
				eventCapacity = input.nextInt();
				if(eventCapacity<1) {
					System.out.println("Event Capacity needs to be atleast 1.");
					break;
				}

				System.out.print("Enter the EventType for the event(1.Conferences|2.Seminars|3.TradeShows) : ");
				typen = input.nextInt();
				if (typen==1) {
					type = "Conferences";
				}
				if (typen==2) {
					type = "Seminars";
				}
				if (typen==3) {
					type = "TradeShows";
				}
				result = FuntionMembers.validateType(type.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}else {
					eventtype = EventType.valueOf(type.toUpperCase());
				}

				status = stub.addEvent(user.toString(), eventId, eventtype.toString(), eventCapacity);
				LOGGER.info(String.format(Constants.LOG_MSG, "addEvent",Arrays.asList(user,eventId,eventtype,eventCapacity),status,Constants.EMPTYSTRING));
				if(status)
					System.out.println("SUCCESS - Event Added Successfully");
				else
					System.out.println("FAILURE = "+eventId+" is already offered in "+eventtype+", only capacity "+ eventCapacity + " would be updated.");
				break;
				
			case 2:
				System.out.print("Enter the event ID : ");
				eventId = input.next().toUpperCase();
				result = FuntionMembers.validateEvent(eventId.trim(), this.user.getcity(),null);
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}

				System.out.print("Enter the EventType for the event(1.Conferences|2.Seminars|3.TradeShows) : ");
				typen = input.nextInt();
				if (typen==1) {
					type = "Conferences";
				}
				if (typen==2) {
					type = "Seminars";
				}
				if (typen==3) {
					type = "TradeShows";
				}
				result = FuntionMembers.validateType(type.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}else {
					eventtype = EventType.valueOf(type.toUpperCase());
				}

				status = stub.removeEvent(user.toString(), eventId, eventtype.toString());
				LOGGER.info(String.format(Constants.LOG_MSG, "removeEvent",Arrays.asList(user,eventId,eventtype),status,Constants.EMPTYSTRING));
				if(status)
					System.out.println("SUCCESS - "+eventId+" removed successfully for "+eventtype+".");
				else
					System.out.println("FAILURE - "+eventId+" is not offered in  "+eventtype+".");
				break;
				
			case 3:
				System.out.print("Enter the EventType for event schedule(1.Conferences|2.Seminars|3.TradeShow) : ");
				typen = input.nextInt();
				if (typen==1) {
					type = "Conferences";
				}
				if (typen==2) {
					type = "Seminars";
				}
				if (typen==3) {
					type = "TradeShows";
				}
				result = FuntionMembers.validateType(type.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}else {
					eventtype = EventType.valueOf(type.toUpperCase());
				}

				
				any = stub.listEventAvailability(user.toString(), eventtype.toString());
				eventMap = (HashMap<String, Integer>) any.extract_Value();
				StringBuilder sb = new StringBuilder();
				sb.append(eventtype).append(" - ");
				eventMap.forEach((k,v)-> sb.append(k).append(" ").append(v).append(", "));
				if(eventMap.size()>0)
					sb.replace(sb.length()-2, sb.length()-1, ".");
				
				LOGGER.info(String.format(Constants.LOG_MSG, "listEventAvailability",Arrays.asList(user, eventtype), eventMap!=null,eventMap));
				if(eventMap!=null)
					System.out.println(sb);
				else
					System.out.println("There was some problem in getting the Event schedule. Please try again later.");
				
				break;
			case 4:
				System.out.print("Enter the Customer ID(eg. MTLC1111) : ");
				customerId = input.next().toUpperCase();
				result = FuntionMembers.validateUser(customerId.trim(), Role.Customer,this.user.getcity());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}
				System.out.print("Enter the Event ID (eg. MTLA2342,OTWE2345,...) : ");
				eventId = input.next().toUpperCase();
				result = FuntionMembers.validateEvent(eventId.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}
				System.out.print("Enter EventType(1.Conferences|2.Seminars|3.TradeShows) : ");
				type = input.next();
				result = FuntionMembers.validateType(type.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}else {
					eventtype = EventType.valueOf(type.toUpperCase());
				}
				any = stub.bookevent(customerId, eventId, eventtype.toString());
				result = (SimpleEntry<Boolean, String>) any.extract_Value();
				
				LOGGER.info(String.format(Constants.LOG_MSG, "BookEvent",Arrays.asList(customerId,eventId,eventtype),result.getKey(),result.getValue()));
				if(result.getKey())
					System.out.println("SUCCESS - "+customerId+" successfully booked in "+eventId+".");
				else
					System.out.println("FAILURE - "+result.getValue());
				
				break;

			case 5:
				System.out.print("Enter the Customer ID(eg. MTLC1111) : ");
				customerId = input.next().toUpperCase();
				result = FuntionMembers.validateUser(customerId.trim(), Role.Customer,this.user.getcity());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}
				
				any = stub.getbookingSchedule(customerId);
				eventList = (HashMap<String, ArrayList<String>>) any.extract_Value();
				
				LOGGER.info(String.format(Constants.LOG_MSG, "getBookingSchedule",Arrays.asList(customerId),eventList!=null,eventList));
				if(eventList!=null)
					System.out.println(eventList);
				else
					System.out.println("There was some problem in getting the event schedule. Please try again later.");
				
				break;
			case 6:
				System.out.print("Enter the Customer ID(eg.MTLC1111) : ");
				customerId = input.next().toUpperCase();
				result = FuntionMembers.validateUser(customerId.trim(), Role.Customer,this.user.getcity());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}
				System.out.print("Enter the Event ID to drop : ");
				eventId = input.next().toUpperCase();
				result = FuntionMembers.validateEvent(eventId.trim(), null,null);
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}
				any = stub.dropevent(customerId, eventId);
				result = (SimpleEntry<Boolean, String>) any.extract_Value();
				
				LOGGER.info(String.format(Constants.LOG_MSG, "dropEvent",Arrays.asList(customerId,eventId),result.getKey(),result.getValue()));
				if(result.getKey())
					System.out.println("SUCCESS - Event successfully dropped for "+customerId+".");
				else
					System.out.println("FAILURE - "+result.getValue());
				
				break;
			case 7:
//swap course
				EventType oldeventtype;
				EventType neweventtype;
				System.out.print("Enter the Customer ID(eg.MTLC1111) : ");
				customerId = input.next().toUpperCase();
				result = FuntionMembers.validateUser(customerId.trim(), Role.Customer,this.user.getcity());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}
				System.out.print("Enter the Event ID to drop(eg. MTLA2342,OTWE2345,...) : ");
				String oldeventId = input.next().toUpperCase();
				result = FuntionMembers.validateEvent(oldeventId.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}

				System.out.print("Enter the EventType for the event(1.Conferences|2.Seminars|3.TradeShows) : ");
				typen = input.nextInt();
				if (typen==1) {
					type = "Conferences";
				}
				if (typen==2) {
					type = "Seminars";
				}
				if (typen==3) {
					type = "TradeShows";
				}
				result = FuntionMembers.validateType(type.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}else {
					oldeventtype = EventType.valueOf(type.toUpperCase());
				}
				System.out.print("Enter the Event ID to book (eg. MTLA2342,OTWE2345,...) : ");
				String neweventId = input.next().toUpperCase();
				result = FuntionMembers.validateEvent(neweventId.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}
				System.out.print("Enter the EventType for the event(1.Conferences|2.Seminars|3.TradeShows) : ");
				typen = input.nextInt();
				if (typen==1) {
					type = "Conferences";
				}
				if (typen==2) {
					type = "Seminars";
				}
				if (typen==3) {
					type = "TradeShows";
				}
				result = FuntionMembers.validateType(type.trim());
				if (!result.getKey()) {
					System.out.println(result.getValue());
					break;
				}else {
					neweventtype = EventType.valueOf(type.toUpperCase());
				}
				
				any = stub.swapEvent(user.toString(), neweventId, neweventtype.toString(), oldeventId, oldeventtype.toString());
				result = (SimpleEntry<Boolean, String>) any.extract_Value();
				
				LOGGER.info(String.format(Constants.LOG_MSG, "swapEvent",Arrays.asList(customerId,neweventId,oldeventId,oldeventtype, neweventtype),result.getKey(),result.getValue()));
				if(result.getKey())
					System.out.println("SUCCESS - Event successfully swapped for "+customerId+".");
				else
					System.out.println("FAILURE - "+result.getValue());
				break;
			case 8:
				break;
			default:
				System.out.println("Please select a valid operation.");
				break;

			}

			System.out.println("\n\n");
			userSelection = displayMenu();
		}
		System.out.println("HAVE A NICE DAY!");
	}

	
	/**
	 * Display menu for manager
	 * @return
	 */
	private int displayMenu() {
		System.out.println("*     Select a operation     *");
		System.out.println("(1) Add a event.");
		System.out.println("(2) Remove a event.");
		System.out.println("(3) List Event Availability.");
		System.out.println("(4) Enroll in event.");
		System.out.println("(5) Get Booking Schedule.");
		System.out.println("(6) Drop a Event.");
		System.out.println("(7) Swap a Event.");
		System.out.println("(8) Quit.");
		System.out.print("Please input the number: ");
		
		return input.nextInt();
	}
	
	/**
	 * Configures the logger
	 * @throws IOException
	 */
	private void setupLogging() throws IOException {
		File files = new File(Constants.MANAGER_LOG_DIRECTORY);
        if (!files.exists()) 
            files.mkdirs(); 
        files = new File(Constants.MANAGER_LOG_DIRECTORY+user+".log");
        if(!files.exists())
        	files.createNewFile();
        allLogger.setup(files.getAbsolutePath());
	}

}
