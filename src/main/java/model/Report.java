package model;

/**
 * Class holds report objects.
 * */
public class Report {

    private String fieldName;
    private int someValueBasedOnReport;

    public Report(String fieldName, int someValueBasedOnReport) {
        this.fieldName = fieldName;
        this.someValueBasedOnReport = someValueBasedOnReport;
    }

    /**
     * @return fieldName
     * */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName
     * */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return someValueBasedOnReport
     * */
    public int getSomeValueBasedOnReport() {
        return someValueBasedOnReport;
    }

    /**
     * @param someValueBasedOnReport
     * */
    public void setSomeValueBasedOnReport(int someValueBasedOnReport) {
        this.someValueBasedOnReport = someValueBasedOnReport;
    }

}
