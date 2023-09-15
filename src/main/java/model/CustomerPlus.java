package model;

/**
 * Class holds the customerplus objects.
 * */
public class CustomerPlus {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String postalCode;
    private String division;
    private String country;
    private String phone;

    public CustomerPlus(int customerId, String customerName, String customerAddress, String postalCode, String division, String country, String phone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.division = division;
        this.country = country;
        this.phone = phone;
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

    public String getDivision() {
        return division;
    }

    /**
     * @param division
     * */
    public void setDivision(String division) {
        this.division = division;
    }

    public String getCountry() {
        return country;
    }

    /**
     * @param country
     * */
    public void setCountry(String country) {
        this.country = country;
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
}
