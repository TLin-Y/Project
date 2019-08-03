package ReplicaHost1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;

import ReplicaHost1Impl.EventSystemImplementation;



public class BackUpThread implements Runnable {
    DatagramPacket packet;
    DatagramSocket socket;
    EventSystemImplementation city;

    public BackUpThread(DatagramSocket socket, DatagramPacket packet , EventSystemImplementation city) {
        this.city = city;
        this.packet = packet;
        this.socket = socket;
    }

    @Override
    public void run() {
        String message = new String(packet.getData(), 0 , packet.getLength());
        String[] ms = message.split(":");
        String info = ms[1];

        try {

            String[] commandori = info.split(" ");
            String[] command = {"addEvent","MTLM0001","MTLA070819","Conferences","3","MTLC0001"};
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
            String result = "";
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
             default :
                 System.out.println("Invalid Command!");
         }

            byte[] ack = result.getBytes();
            DatagramPacket packet = new DatagramPacket(ack, 0, ack.length, this.packet.getAddress(), 5001);
            socket.send(packet);

        }  catch (Exception e) {
            e.printStackTrace();
        }


        //socket.close();不能关闭
    }

}
