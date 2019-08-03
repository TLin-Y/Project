package ReplicaHost1Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import functions.City;


public class EventSystemSTHWrong {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private City city;

	private ReentrantLock rl;


	//In memory database
	private HashMap<String, HashMap<String, HashMap<String, Object>>> cityDatabase;


	public Logger logger;
	public boolean bug;

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Course>> compCourseDatabase = new ConcurrentHashMap<String, ConcurrentHashMap<String, Course>>();

    public EventSystemSTHWrong(){ }

    public String addEvent(String eventID, String eventtype) {
        return "Fail";
    }

    public String removeEvent(String eventID, String eventtype) {
        return "Fail";
    }

    public String[] listEventAvailability(String eventtype) {
        List<String> courseList = new ArrayList<>();

        courseList.add("Wrong listeventavailability has been called");

        return translateStringArray(courseList);
    }

    private String[] translateStringArray(List<String> courseList) {
        String[] res = new String[courseList.size()];
        for(int i = 0 ; i < courseList.size() ; i ++){
            res[i] = courseList.get(i);
        }
        return res;
    }


    public String bookevent(String customerID, String eventID, String eventtype) {
        return "Fail";
    }

    public String dropevent(String customerID, String eventID) {
       return "Fail";
    }

    public String[] getbookingSchedule(String customerID) {

        List<String> res = new ArrayList<>();
        res.add(new String("Wrong getclassschedule has been called"));

        return translateStringArray(res);
    }

    public String swapEvent(String customerID, String neweventID, String oldeventID) {
        return "Fail";
    }

}

