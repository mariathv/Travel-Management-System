package application.Model;

public class Notifications {
    String date;
    String Message;
    String agency;
    String tag;
    String type;

    public Notifications(String date, String message, String agency, String tag, String type) {
        this.date = date;
        this.Message = message;
        this.agency = agency;
        this.tag = tag;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
