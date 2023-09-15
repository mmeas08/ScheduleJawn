package model;

/**
 * Class holds firstleveldivsion objects.
 * */
public class FirstLevelDivision {

    private int divisionId;
    private int countryId;
    private String division;

    public FirstLevelDivision(int divisionId, int countryId, String division) {
        this.divisionId = divisionId;
        this.countryId = countryId;
        this.division = division;
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

    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId
     * */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
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


    public String toString(){
        return (division);
    }

}
