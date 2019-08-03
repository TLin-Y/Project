package ReplicaHost1;

import java.io.IOException;
import java.net.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import ReplicaHost1Impl.EventSystemImplementation;
import ReplicaHost1Impl.EventSystemSTHWrong;


public class Replica1 {

    public boolean bugFree;
    public Logger logger;
    public EventSystemImplementation MTLServer;
    public EventSystemImplementation OTWServer;
    public EventSystemImplementation TORServer;
    public EventSystemSTHWrong MTLWrong;
    public EventSystemSTHWrong OTWWrong;
    public EventSystemSTHWrong TORWrong;

    public Replica1(Logger logger , EventSystemImplementation MTLServer, EventSystemImplementation OTWServer, EventSystemImplementation TORServer, 
    		EventSystemSTHWrong MTLWrong, EventSystemSTHWrong OTWWrong, EventSystemSTHWrong TORWrong){
        this.logger = logger;
        this.MTLServer = MTLServer;
        this.OTWServer = OTWServer;
        this.TORServer = TORServer;
        this.MTLWrong = MTLWrong;
        this.OTWWrong = OTWWrong;
        this.TORWrong = TORWrong;
        this.bugFree = true;
    }

    private void reRunReplica(int RepBackPort, int rmBackPort){
        //TODO:请求RM的backUpQ，得到history message, rerun the message
        try {
            InetAddress address = InetAddress.getByName("localhost");
            DatagramSocket socket = new DatagramSocket(RepBackPort);
            byte[] data = Failure.BackUp.toString().getBytes();
            DatagramPacket packet = new DatagramPacket(data,0 ,data.length,address, rmBackPort);
            socket.send(packet);

            while (true){
                byte[] buffer = new byte[1024];
                DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet1);
                String msg = new String(packet1.getData(), 0 , packet1.getLength());
                String department = msg.split(":")[0];

                Thread thread = new Thread(new BackUpThread(socket, packet1, getDepartment(department)));
                thread.start();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startReplica(int replica1Port) throws IOException {
        DatagramSocket socket = new DatagramSocket(replica1Port);
        DatagramPacket packet = null;
        byte[] data = null;
        logger.info(" Replica Server Start");
        while(true)
        {
            data = new byte[1024];
            packet = new DatagramPacket(data, data.length);
            socket.receive(packet);
            logger.info("Replica Recv Message :" + new String(packet.getData(), 0, packet.getLength()));

            String recvMsg = new String(packet.getData(), 0 , packet.getLength());
            String department = recvMsg.split(":")[0];

            Thread thread = new Thread(new WorkerThread(socket, packet, getDepartment(department), 
            		getWrongDepartment(department), bugFree));
            thread.start();
        }
    }

    private EventSystemImplementation getDepartment(String department) {
        if (department.equals("MTL"))
            return this.MTLServer;
        else if(department.equals("OTW"))
            return this.OTWServer;
        else
            return this.TORServer;
    }

    private EventSystemSTHWrong getWrongDepartment(String department) {
        if (department.equals("MTL"))
            return this.MTLWrong;
        else if(department.equals("OTW"))
            return this.OTWWrong;
        else
            return this.TORWrong;
    }

    public static void configLogger(String department , Logger logger) throws IOException {
        logger.setLevel(Level.ALL);
        FileHandler MTLFileHandler = new FileHandler(department + ".log");
        MTLFileHandler.setFormatter(new LoggerFormatter());
        logger.addHandler(MTLFileHandler);
    }


    public void startSoftFailPort(int port) throws IOException {
        DatagramSocket socket = new DatagramSocket(port);
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (true){
            socket.receive(packet);
            this.bugFree = true;
        }
    }

    public static void main(String[] args) throws IOException {
        Logger MTLLogger = Logger.getLogger("MTL.server1.log");
        configLogger("MTLServer1",MTLLogger);

        Logger OTWLogger = Logger.getLogger("OTW.server1.log");
        configLogger("OTWServer1",OTWLogger);

        Logger TORLogger = Logger.getLogger("TOR.server1.log");
        configLogger("TORServer1",TORLogger);

        Logger replicaLogger = Logger.getLogger("replica1.log");
        configLogger("replica1", replicaLogger);

        EventSystemImplementation MTLServer = new EventSystemImplementation("MTL",MTLLogger);

        EventSystemImplementation OTWServer = new EventSystemImplementation("OTW", OTWLogger);

        EventSystemImplementation TORServer = new EventSystemImplementation("TOR", TORLogger);

        EventSystemSTHWrong MTLWrong = new EventSystemSTHWrong();
        EventSystemSTHWrong OTWWrong = new EventSystemSTHWrong();
        EventSystemSTHWrong TORWrong = new EventSystemSTHWrong();

        Replica1 replica1 = new Replica1(replicaLogger , MTLServer , OTWServer , TORServer, MTLWrong, OTWWrong, TORWrong);

        Runnable replicaTask = () -> {
            try {
                replica1.startReplica(1111);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable softwareListener = () -> {
            try {
                replica1.startSoftFailPort(8881);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };


        Thread t1 = new Thread(replicaTask);
        Thread t2 = new Thread(softwareListener);

        t1.start();
        t2.start();

        replica1.reRunReplica(8081, 5001);

    }
}
