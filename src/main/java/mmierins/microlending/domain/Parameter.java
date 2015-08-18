package mmierins.microlending.domain;

import javax.persistence.Entity;

@Entity
public class Parameter extends IdEntity {

    private String key;
    private String value;

    public Parameter() {

    }

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getValueAsLong() {
        return Long.valueOf(value);
    }

    public Double getValueAsDouble() {
        return Double.valueOf(value);
    }

}
