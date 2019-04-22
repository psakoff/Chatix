public class User {
    private String name;
    private ServerListener serverListener;
    private String type;
    private String platform;
    private User contact;

    public User(String name,String type,String platform, ServerListener serverListener){
        this.name = name;
        this.serverListener = serverListener;
        this.type = type;
        this.platform = platform;
        this.contact = null;
    }

    public ServerListener getServerListener() {
        return serverListener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getContact() {
        return contact;
    }

    public void setContact(User contact) {
        this.contact = contact;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }
}
