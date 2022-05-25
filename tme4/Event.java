//: innerclasses/controller/tme3.Event.java
// The common methods for any control event.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @date  : Oct. 21, 2006
 *
 * Description: tme3.Event abstract class
 *
 */

package tme4;

import control.*;
import java.io.*;

public abstract class Event implements Runnable{
  private long eventTime;
  //protected final long delayTime;
  protected GreenhouseControls greenhouseControls;
  protected boolean suspended = false;
  public Event(GreenhouseControls gc, long eventTime) {
    this.eventTime = eventTime;
    this.greenhouseControls = gc;
  }
  public void run() {
    try {
      synchronized(this) {
        while(suspended) {
          try {
            wait();
          } catch(InterruptedException e) {
              e.printStackTrace();
          }
        }
      }
      Thread.sleep(eventTime);
    } catch(InterruptedException e) {
      e.printStackTrace();
    }

    try {
      this.action();
    } catch(Controller.ControllerException e) {
          greenhouseControls.shutdown("Error");
          e.printStackTrace();
      }
  }

  public void suspend() {
    suspended = true;
  }

  public synchronized void resume() {
    suspended = false;
    notify();
  }
  /*public void start() { // Allows restarting
    eventTime = System.currentTimeMillis() + delayTime;
  }*/
  public boolean ready() {
    return System.currentTimeMillis() >= eventTime;
  }
  public abstract void action() throws Controller.ControllerException;
} ///:~
