package ReplicaHost1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import ReplicaHost1Impl.EventSystemImplementation;
import ReplicaHost1Impl.EventSystemSTHWrong;


public class WorkerThread implements Runnable {

    DatagramPacket packet;
    DatagramSocket socket;
    EventSystemImplementation city;
    EventSystemSTHWrong cityWrong;
    boolean bug;

    public WorkerThread(DatagramSocket socket, DatagramPacket packet , EventSystemImplementation city, 
    		EventSystemSTHWrong cityWrong, boolean bug) {
        this.city = city;
        this.packet = packet;
        this.socket = socket;
        this.cityWrong = cityWrong;
        this.bug = bug;
    }

    @Override
    public void run() {
        InetAddress address = null;
        String message = new String(packet.getData(), 0 , packet.getLength());
        String info = null;
        if (!message.equals("Hi")){
            String[] ms = message.split(":");
            info = ms[1];
        } else {
            info = "Hi";
        }

        int port = 8800;
        byte[] data2 = null;
        DatagramPacket packet2 = null;

        try {

            String[] commandori = info.split(" ");
            //test command---------------------------------------------------------------
            String[] command = {"addEvent","MTLM0001","MTLA070819","Conferences","3","MTLC0001"};
            String result = "";
            /**
            sendRequest(sb.append(City)
                    .append(":")
                command[0]
                    .append("addEvent")
                command[1]
                	ManagerID
                command[2]
 					EventId
                command[3] 
                	event type;
               	command[4]
               		event compacity;
               	command[5]
               		customerID;
               	command[6]
               		new eventID;
               	command[7]
               		new eventtype;
               	command[8]
               		old eventID;
               	command[9]
               		old eventtype;               		
             */
            
            
            if (!bug){
                switch(command[0]) {
                    case "addEvent" :
                        result = cityWrong.addEvent(command[1],command[2]);
                        city.addEvent(command[1],command[2], command[3], Integer.parseInt(command[4]));
                        break;
                    case "removeEvent" :
                        result = cityWrong.removeEvent(command[1],command[2]);
                        city.removeEvent(command[1],command[2],command[3]);
                        break;
                    case "listEventAvailability" :
                        city.listEventAvailability(command[1],command[3]);
                        String[] res = cityWrong.listEventAvailability(command[1]);
                        StringBuilder r = new StringBuilder();
                        Arrays.stream(res).forEach(record -> r.append(record).append(" "));
                        result = r.toString().trim();
                        break;
                    case "bookevent" :
                        city.bookevent(command[5], command[2], command[3]);
                        result = cityWrong.bookevent(command[1], command[2], command[3]);
                        break;
                    case "dropevent" :
                        city.dropevent(command[5], command[2]);
                        result = cityWrong.dropevent(command[1], command[2]);
                        break;
                    case "getbookingSchedule" :
                        StringBuilder sb = new StringBuilder();
                        city.getbookingSchedule(command[5]);
                        Arrays.stream(cityWrong.getbookingSchedule(command[1])).forEach(record -> sb.append(record).append(" "));
                        result = sb.toString().trim();
                        break;
                    case "swapEvent" :
                        city.swapEvent(command[5],command[6],command[7],command[8],command[9]);
                        result = cityWrong.swapEvent(command[1],command[2],command[3]);
                        break;
                    case "Hi" :
                        ReplyEcho(this.packet);
                        break;
                    default :
                        System.out.println("Invalid Command!");
                }
            }else {
            	boolean result1;
            	SimpleEntry<Boolean, String> result2;
                switch(command[0]) {
                    case "addEvent" :
                        result1 = city.addEvent(command[1],command[2], command[3], Integer.parseInt(command[4]));
                        if (result1) {
                        	 result = "Add Event Complete!";
						}else {
                        result = "Add Event fail!";}
                        break;
                    case "removeEvent" :
                        result1 = city.removeEvent(command[1],command[2],command[3]);
                        if (result1) {
                       	 result = "Remove Event Complete!";
						}else {
                       result = "Remove Event fail!";}
                        break;
                    case "listEventAvailability" :
                    	HashMap<String, Integer> res = city.listEventAvailability(command[1],command[3]);
                        result = res.toString();
                        break;
                    case "bookevent" :
                    	result2 = city.bookevent(command[5], command[2], command[3]);
                    	result = result2.toString();
                        break;
                    case "dropevent" :
                        result2 = city.dropevent(command[5], command[2]);
                        result = result2.toString();
                        break;
                    case "getbookingSchedule" :
                    	HashMap<String, ArrayList<String>> result3 = city.getbookingSchedule(command[5]);
                        result = result3.toString();
                        break;
                    case "swapEvent" :
                        result2 = city.swapEvent(command[5],command[6],command[7],command[8],command[9]);
                        result = result2.toString();
                        break;
                    case "Hi" :
                        ReplyEcho(this.packet);
                        break;
                    default :
                        System.out.println("Invalid Command!");
                }
            }


            address = packet.getAddress();
            port = packet.getPort();

            data2 = result.getBytes();
            packet2 = new DatagramPacket(data2, data2.length, address, port);
            socket.send(packet2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //socket.close();不能关闭
    }

    private void ReplyEcho(DatagramPacket packet) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] reply = "Reply Hi".getBytes();
            DatagramPacket packet1 = new DatagramPacket(reply, 0, reply.length, packet.getAddress(), packet.getPort());
            socket.send(packet1);
            socket.close();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
