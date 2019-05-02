/* [Flight.java]
 * Contains flight information
 * Albert Quon
 * 04/30/2019
 */

import java.util.Date;


public class Flight implements Comparable<Flight>{
    private String airline, location;
    private Date date;
    private boolean arrival;

    Flight(String airline, boolean arrival, String from, String to, Date date){
        this.airline = airline;
        this.arrival = arrival;
        this.location = location;
        this.date = date;
    }

    public String getAirline() {
        return this.airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public boolean isArrival() {
        return arrival;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate(){ return this.date;}

    @Override
    public int compareTo(Flight other) {
        if (isArrival() == other.isArrival()){
            //check date
            if (this.date.compareTo(other.getDate()) > 0) {
                return 1;
            } else if (this.date.compareTo(other.getDate()) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
        return location.compareTo(other.getLocation());

        //then see if comparing departures or arrival
        //then compare
    }
}
