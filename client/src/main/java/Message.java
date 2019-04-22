import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty("user")private String user;
    @JsonProperty("value")private String value;
    @JsonProperty("platform")private String platform;
    @JsonProperty("typeOfUser")private String typeOfUser;
    @JsonProperty("sendTo")private String sendTo;
    @JsonCreator
    public  Message (@JsonProperty("user")String user, @JsonProperty("platform")String platform,
                     @JsonProperty("value")String value, @JsonProperty("typeOfUser")String typeOfUser){
        this.platform = platform;
        this.user = user;
        this.value = value;
        this.typeOfUser = typeOfUser;
    }

    public String getPlatform() {
        return platform;
    }

    public String getUser() {
        return user;
    }

    public String getValue() {
        return value;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
}