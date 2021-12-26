package edu.vt;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

// Chronoline View Component Data
@Named("chronolineView")
public class ChronolineView {

    private List<Event> events;
    private List<Event> events2;

    @PostConstruct
    public void init() {
        events = new ArrayList<>();
        events.add(new Event("SignUp as a Rider", "Step 1", "You can register as a rider in order to book a ride."));
        events.add(new Event("Login as a Rider", "Step 2", "Enter email address and password to login as a rider."));
        events.add(new Event("Find a Ride", "Step 3", "Enter the desired start, end location and the time to find the rides."));
        events.add(new Event("Join a Ride", "Step 4", "Join the desired ride from a list of available rides."));
        events.add(new Event("View Rides History", "Step 5", "You can also view your rides history and rate the rides once completed."));

        events2 = new ArrayList<>();
        events2.add(new Event("SignUp as a Driver", "Step 1", "You can register as a driver in order to create a new ride."));
        events2.add(new Event("Login as a Driver", "Step 2", "Enter email address and password to login as a driver."));
        events2.add(new Event("Create a Ride", "Step 3", "Enter the desired start, end location and the time to create a ride."));
        events2.add(new Event("View Ride History", "Step 4", "Driver can also view the previous created rides and the ratings he has received."));


    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Event> getEvents2() {
        return events2;
    }

    public static class Event {
        private String header;
        private String stepNo;
        private String info;

        public Event() {
        }

        public Event(String header, String stepNo, String info) {
            this.header = header;
            this.stepNo = stepNo;
            this.info = info;
        }


        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getStepNo() {
            return stepNo;
        }

        public void setStepNo(String stepNo) {
            this.stepNo = stepNo;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
