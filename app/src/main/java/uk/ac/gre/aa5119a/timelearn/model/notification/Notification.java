package uk.ac.gre.aa5119a.timelearn.model.notification;

public class Notification {

    private int type;
    private Object notification;

    public Notification(int type, Object notification) {
        this.type = type;
        this.notification = notification;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getNotification() {
        return notification;
    }

    public void setNotification(Object notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type=" + type +
                ", notification=" + notification +
                '}';
    }
}
