package org.clintrorick.gis.domain;

public class CoordinateExistence {
    private boolean coordinateExistsInDb;
    private String errorMessage;
    private boolean hasErrors;

    public boolean isCoordinateExistsInDb() {
        return coordinateExistsInDb;
    }

    public void setCoordinateExistsInDb(boolean coordinateExistsInDb) {
        this.coordinateExistsInDb = coordinateExistsInDb;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }
}
