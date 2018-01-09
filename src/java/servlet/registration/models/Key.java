package servlet.registration.models;

import java.sql.Date;

public class Key {

    private String key;
    private Integer value;
    private Date validUntil;

    public Key(String key, Integer value, Date validUntil) {
        this.key = key;
        this.value = value;
        this.validUntil = validUntil;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }


}
