package model;


/**
 * Class holds country objects.
 * */
public class Country {

    private int countryId;
    private String country;

    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /**
     * @return countryId
     * */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId
     * */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * @return country
     * */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     * */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return country
     * */
    public String toString(){
        return (country);
    }

}
