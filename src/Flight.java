/* [Flight.java]
 * Contains flight information
 * Albert Quon
 * 04/30/2019
 */



public class Flight implements Comparable<Flight>{
    private String airline;
    private String timeIn, timeOut;
    private String from, to, date;

    Flight(String airline, String timeIn, String timeOut, String from, String to, String date){
        this.airline = airline;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getAirline() {
        return this.airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getTimeIn() {
        return this.timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate(){ return this.date;}

    @Override
    public int compareTo(Flight other) {
        //check date
        //then see if comparing departures or arrival
        //then compare
        return 0;
    }
}
