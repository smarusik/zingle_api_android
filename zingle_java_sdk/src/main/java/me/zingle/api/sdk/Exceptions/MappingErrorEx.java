package me.zingle.api.sdk.Exceptions;

/**
 * Runtime exception. Indicates, that error occurred while mapping JSON source to some Zingle model object.
 */
public class MappingErrorEx extends RuntimeException {
    private String objectName;
    private String mappedSource;
    private String errorPlace;

    public MappingErrorEx(String objectName, String mappedSourse, String errorPlace) {
        this.objectName = objectName;
        this.mappedSource = mappedSourse;
        this.errorPlace = errorPlace;
    }

    public String getErrorPlace() {
        return errorPlace;
    }

    public void setErrorPlace(String errorPlace) {
        this.errorPlace = errorPlace;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getMappedSource() {
        return mappedSource;
    }

    public void setMappedSource(String mappedSource) {
        this.mappedSource = mappedSource;
    }

    @Override
    public String getMessage(){
        return String.format("%s mapping failed at:%s\nJSON source: %s",objectName,errorPlace, mappedSource);
    }
}
