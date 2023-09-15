package model;


/**
 * Class holds data for contact objects
 * */
public class Contact {
    private int contactId;
    private String contactName;
    private String contactEmail;

    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * @return contactId
     * */
    public int getContactId() {
        return contactId;
    }

    /**
     * @param contactId
     * */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @return contactName
     * */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName
     * */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return contactEmail
     * */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail
     * */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * @return contactId + " " + contactName
     * */
    public String toString(){
        return (contactId + " " + contactName);
    }
}
