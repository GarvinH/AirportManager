package Trees;

public class Flight implements Comparable<Flight>{
    private String airline;
    private String timeIn, timeOut;
    private String from, to;

    Flight(String airline, String timeIn, String timeOut, String from, String to){
        this.airline = airline;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.from = from;
        this.to = to;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    @Override
    public int compareTo(Flight other) {
        return 0;
    }
}
