package za.co.mtn.ti.notifier;

public class SMSNotification {
    private String heading;
    private String message;
    private int id;
    public SMSNotification(String heading, String message, Integer id) {
        super();
        this.heading = heading;
        this.message = message;
        this.id = id;
    }

    public SMSNotification(String notification) {
        super();
        int delimPositions[] = {notification.indexOf('|'),notification.lastIndexOf('|')};
        this.id = Integer.parseInt(notification.substring(0, delimPositions[0]));
        this.heading = notification.substring(delimPositions[0]+1, delimPositions[1]);
        this.message = (notification.substring(delimPositions[1]+1));
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
