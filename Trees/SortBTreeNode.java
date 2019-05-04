/* [Flight.java]
 * Contains flight information
 * Albert Quon
 * 04/30/2019
 */

import java.util.Date;

public class Flight implements Comparable<Flight>{
    private String airline, location, name;
    private Date date;
    private boolean arrival;
    private String status;

    /**
     *
     * @param airline
     * @param name
     * @param arrival
     * @param location
     * @param date
     */
    Flight(String airline, String name, boolean arrival, String location, Date date, String status){
        this.airline = airline;
        this.arrival = arrival;
        this.location = location;
        this.date = date;
        this.name = name;
        this.status = status;
    }

    /**
     *
     * @return
     */
    public String getAirline() {
        return this.airline;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return this.status;
    }

    /**
     *
     * @return
     */
    public boolean isArrival() {
        return arrival;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public Date getDate(){
        return this.date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    @Override
    /**
     *
     */
    public int compareTo(Flight other) {
        if (isArrival() && other.isArrival()){
            //check date
            int compare = location.compareTo(other.getLocation());
            if (compare == 0) { //sort by flight name instead
                return date.compareTo(other.getDate());
            } else {
                return compare;
            }
        } else {
            return -2; // its not valid to check
        }

    }
}
