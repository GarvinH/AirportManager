/* [Flight.java]
 * Contains flight information
 * Albert Quon
 * 04/30/2019
 */


public class Flight implements Comparable<Flight>{
    private String airline, location, name, date, time;
    private String status;

    /**
     *
     * @param airline
     * @param name
     * @param location
     * @param date
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
    public String getDate(){
        return this.date;
    } // YYYY/MM/DD

    /**
     *
     * @param date
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getTime() {
        return this.time;
    }

    /**
     *
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
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
        int timeA, timeB;
        String otherDate = other.getDate();
        // compare location
        int compare = location.compareTo(other.getLocation());
        if (compare == 0) {
            //sort by times if destination/origin is the same
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
                        // compare flight times
                        timeA = Integer.parseInt(time);
                        timeB = Integer.parseInt(other.getTime());
                        if (timeA == timeB) {
                            //compare flight names
                            compare = name.substring(0,2).compareTo(other.getName().substring(0,2));
                            if (compare == 0) {
                                // compare flight numbers
                                timeA = Integer.parseInt(name.substring(3));
                                timeB = Integer.parseInt(other.getName().substring(3));
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
