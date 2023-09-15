package model;

/**
 * Class holds customer objects.
 * */
public class Customer {
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String postalCode;
    private String phone;
    private int divisionId;

    public Customer(int customerId, String customerName, String customerAddress, String postalCode, String phone, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     * */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     * */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param customerAddress
     * */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode
     * */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     * */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId
     * */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String toString(){
        return (customerId + " " + customerName);
    }



}
