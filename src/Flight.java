/* [Flight.java]
 * Contains flight information
 * Albert Quon
 * 04/30/2019
 */


public class Flight implements Comparable<Flight>, java.io.Serializable {
    private String airline, location, name, date, time;
    private String status;

    /**
     * Constructor for a flight
     * @param airline Airline of the flight
     * @param name Flight name
     * @param location Destination or origin
     * @param date Flight date
     * @param time Departure/Arrival of flight
     * @param status Status of the flight
     */
    Flight(String name, String airline, String location, String date, String time, String status){
        this.airline = airline;
        this.location = location;
        this.date = date;
        this.time = time;
        this.name = name;
        this.status = status;
    }

    /**
     * Gets the airline
     * @return The airline name
     */
    public String getAirline() {
        return this.airline;
    }

    /**
     * Gets the status of the flight
     * @return The status of the flight
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Gets the assigned location of the flight
     * @return The flight's destination/origin
     */
    public String getLocation() {
        return location;
    }

    /**
     * Change status of the flight
     * @param status The flight's new status
     */
    public void setStatus(String status) {
        this.location = location;
    }

    /**
     * Gets the date of the flight's departure/arrival
     * @return The date
     */
    public String getDate(){
        return this.date;
    } // YYYY/MM/DD

    /**
     * Change the date of the flight
     * @param date The flight's new date
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Get the time of departing/arriving of the flight
     * @return The time of the flight
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Change the time of the flight
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets the flight name
     * @return The flight name
     */
    public String getName() {
        return name;
    }

    @Override
    /**
     * @Override
     * Compares with another flight based on location, year, month, day, and name
     * @param other The flight being compared
     * @return integer value either negative, zero, or positive based on the comparison
     */
    public int compareTo(Flight other) {
        int timeA, timeB; // compares times
        String otherDate = other.getDate();

        // compare location
        int compare = location.compareTo(other.getLocation());
        if (compare == 0) {
            //sort by date if destination/origin is the same
            //compare year
            timeA = Integer.parseInt(date.substring(0,date.indexOf("/")));
            timeB = Integer.parseInt(otherDate.substring(0,otherDate.indexOf("/")));
            if (timeA == timeB) {
                //compare month
                timeA = Integer.parseInt(date.substring(date.indexOf("/")+1,date.lastIndexOf("/")));
                timeB = Integer.parseInt(otherDate.substring(otherDate.indexOf("/")+1,otherDate.lastIndexOf("/")));
                if (timeA == timeB) {
                    //compare day
                    timeA = Integer.parseInt(date.substring(date.lastIndexOf("/")+1));
                    timeB = Integer.parseInt(otherDate.substring(otherDate.lastIndexOf("/")+1));
                    if (timeA == timeB) {
                        // if date is the same, then compare flight times
                        // compare flight times
                        timeA = Integer.parseInt(time);
                        timeB = Integer.parseInt(other.getTime());
                        if (timeA == timeB) {
                            //sort by names if times are the same
                            //compare flight names
                            compare = name.substring(0,2).compareTo(other.getName().substring(0,2));
                            if (compare == 0) {
                                // compare flight numbers
                                timeA = Integer.parseInt(name.substring(2));
                                timeB = Integer.parseInt(other.getName().substring(2));
                                return timeA - timeB;
                            }
                        } else {
                            return compare;
                        }
                    } else {
                        return timeA - timeB;
                    }
                } else {
                    return timeA - timeB;
                }
            } else {
                return timeA - timeB;
            }
        }
        return compare;

    }
}
