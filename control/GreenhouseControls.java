//: innerclasses/GreenhouseControls.java
// This produces a specific application of the
// control system, all in a single class. Inner
// classes allow you to encapsulate different
// functionality for each type of event.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer,
 *		SCIS, Athabasca University
 *
 * Assignment: TME4
 * @author: Steve Leung
 * @author: Steven Hamilton
 * @date  : Oct 21, 2005
 * @date  : May 20, 2022
 *
 */

package control;

import java.io.*;
import tme4.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * GreenhouseControls contains status and methods in GreenhouseControls that
 * control what actions will occur.
 *
 */
public class GreenhouseControls extends Controller implements Serializable, Fixable {
  private Set<TwoTuple> status= new HashSet<>();
  private String eventsFile = "unspecified file name";

  public GreenhouseControls(){
      status.add(new TwoTuple<String, Integer>( "errorcode", 0));
      status.add(new TwoTuple<String, Boolean>( "light", false));
      status.add(new TwoTuple<String, Boolean>( "water", false));
      status.add(new TwoTuple<String, Boolean>( "fans", false));
      status.add(new TwoTuple<String, Boolean>( "windowok", false));
      status.add(new TwoTuple<String, Boolean>( "poweron", false));
      status.add(new TwoTuple<String, String>( "thermostat", "Day"));
      status = Collections.synchronizedSet(status);
  }

  void issueOccurred(int errorcode, String errormessage) throws ControllerException { //handles the emergency shutdown procedure
      setErrorcode(errorcode);
      System.out.println("Error code: " + errorcode);

      SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
      Date date = new Date(System.currentTimeMillis());
      System.out.println(formatter.format(date));

      try { //writes to error.log
          String dir = System.getProperty("user.dir");

          File errorLog = new File(dir);
          errorLog.createNewFile();

          FileWriter writer = new FileWriter("error.log");
          writer.write(formatter.format(date) + "\nError code: " + errorcode + "\nError: " + errormessage);
          writer.close();

      } catch (IOException e){
          System.out.println("An error occurred with \"error.log\"");
      }

      log();
      shutdown(errormessage);
      throw new ControllerException(errormessage);
  }

    @Override
    public void shutdown(String message) {
      System.out.println("Initializing emergency shutdown");
      System.exit(0);
    }

    public static void printUsage() {
    System.out.println("Correct format: ");
    System.out.println("  java GreenhouseControls -f <filename>, or");
    System.out.println("  java GreenhouseControls -d dump.out");
  }

  public void log() { //logs data to dump.out
      try {
          FileOutputStream fileOutS = new FileOutputStream("dump.out");
          ObjectOutputStream outputStream = new ObjectOutputStream(fileOutS);

          outputStream.writeObject(GreenhouseControls.this);

          outputStream.close();
          fileOutS.close();

      } catch (FileNotFoundException e) {
          System.out.println("File not found");
      } catch (IOException e) {
          System.out.println("Error initializing stream");
      }
  }

  public void fix() {
      if(getErrorcode() == 1) { //Window Malfunction
          setStatus("windowok", true);
          setErrorcode(0);
      } else if (getErrorcode() == 2) {  //Power Out
          setStatus("poweron", true);
          setErrorcode(0);
      }
  }

    public void run(){
        List<Event> eventList = getEventList();
        ExecutorService exec = Executors.newCachedThreadPool();
        if(eventList.size() == 1){
            exec.submit(eventList.get(0));
            eventList.remove(eventList.get(0));
        } else {
            for(Event e : eventList){
                exec.submit(e);
            }
        }
        exec.shutdown();

    while(eventList.size() > 0){
        for(int i = 0; i < eventList.size(); i++){
            exec.submit(eventList.get(i));
        }
    }
      Thread[] threads = new Thread[eventList.size()];
      while(eventList.size() > 0)
        for(int i = 0; i < eventList.size(); i++){
            threads[i] = new Thread(eventList.get(i));
            threads[i].start();
            eventList.remove(i);
        }
    }

//---------------------------------------------------------
//GETTERS AND SETTERS

    public TwoTuple getStatus(String eventName) {
        for(TwoTuple tt : status){
            //Check for the eventName and sets errorNum
            if(tt.getFirst().equals(eventName)){
                return tt;
            }
        }
      return new TwoTuple("error", "error");
    }

    public void setStatus(String eventName, Boolean newStatus) { //only for boolean statuses
        for(TwoTuple tt : status){
            //Check for the eventName and sets value for event
            if(tt.getFirst().equals(eventName)){
                tt.setSecond(newStatus);
            } else {
                System.out.println("error: cannot find " + eventName + " event.");
            }
        }
    }

    public TwoTuple getThermostatStatus() {
        for(TwoTuple tt : status){
            //Check for the eventName and sets errorNum
            if(tt.getFirst().equals("thermostat")){
                return tt;
            }
        }
        return new TwoTuple("error", "error");
    }

    public void setThermostatStatus(String newStatus) { //only for boolean statuses
        for(TwoTuple tt : status){
            //Check for the eventName and sets value for event
            if(tt.getFirst().equals("thermostat")){
                tt.setSecond(newStatus);
            } else {
                System.out.println("error: cannot find thermostat event.");
            }
        }
    }

    public Integer getErrorcode() {
        int errorNum = 0;
        //Iterate through the status Set
        for(TwoTuple tt : status){
            //Check for the name "errorcode" and sets errorNum
            if(tt.getFirst().equals("errorcode")){
                errorNum = (Integer) tt.getSecond();
                break;
            }
        }
        return errorNum;
    }

    public void setErrorcode(Integer errorcode) {
        //Iterate through the status Set
        for(TwoTuple tt : status){
            //Check for the name "errorcode" and sets errorNum
            if(tt.getFirst().equals("errorcode")){
                tt.setSecond(errorcode);
                break;
            }
        }
    }

    public Fixable getFixable() {
        if(getErrorcode() == 1) { //Window Malfunction
            Fixable fixWindow = new FixWindow(0);
            return fixWindow;
        } else  {  //(if (getErrorcode() == 2)) Power Out
            Fixable powerOn = new PowerOn(0);
            return powerOn;
        }
    }


//---------------------------------------------------------

    public static void main(String[] args) {
        GreenhouseControls gc = null;
        try {
            String option = args[0];
            String filename = args[1];

            if (!(option.equals("-f")) && !(option.equals("-d"))) {
                System.out.println("Invalid option");
                printUsage();
            }

            gc = new GreenhouseControls();

            if (option.equals("-f")) {
                gc.addEvent(new Restart(0, filename));
            }

            if (option.equals("-d")) {
                System.out.println("Reloading a previous state...");
                try {
                    FileInputStream fileIn = new FileInputStream("dump.out");  //this does input
                    ObjectInputStream objIn = new ObjectInputStream(fileIn);

                    // Read objects
                    gc = (GreenhouseControls) objIn.readObject();

                    gc.addEvent(new Restore(0, gc.eventsFile));

                    objIn.close();
                    fileIn.close();
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found");
                } catch (IOException e) {
                    System.out.println("Error initializing stream");
                }
            }

            gc.run();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid number of parameters");
            printUsage();
        }
    }

} ///:~
