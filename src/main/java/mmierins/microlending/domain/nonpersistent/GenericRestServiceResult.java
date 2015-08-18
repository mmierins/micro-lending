package mmierins.microlending.domain.nonpersistent;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mmierins.microlending.misc.View;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class GenericRestServiceResult {

    @JsonView(View.History.class)
    private String message;
    @JsonView(View.History.class)
    private String description;
    @JsonView(View.History.class)
    private int code;

    GenericRestServiceResult() { }

    public GenericRestServiceResult(String message) {
        this.message = message;
    }

    public GenericRestServiceResult(String message, String description, int code) {
        this(message);
        this.description = description;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
