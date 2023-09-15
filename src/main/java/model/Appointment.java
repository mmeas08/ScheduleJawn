package model;

import java.time.LocalDateTime;


/** Class contains appointment objects.
 * */
public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int customerId;
    private int userId;
    private int contactId;


    /**
     * constructor for appointment
     * */
    public Appointment(int appointmentId, String title, String description, String location, String type,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * @return appointmentId
     * */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId
     * */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return title
     * */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return description
     * */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     * */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return location
     * */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     * */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return type
     * */
    public String getType() {
        return type;
    }

    /**
     * @param type
     * */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return startDateTime
     * */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime
     * */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return endDateTime
     * */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime
     * */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return customerId
     * */
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return userId
     * */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return contactId
     * */
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}

