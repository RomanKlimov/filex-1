package servlet.registration.models;

public class UserFile {
    private int id;
    private int user_id;
    private String root_link;
    

    public UserFile(int id, int user_id, String root_link) {
        this.id = id;
        this.user_id = user_id;
        this.root_link = root_link;
    }
    public UserFile(int user_id, String root_link) {
        this.user_id = user_id;
        this.root_link = root_link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRoot_link() {
        return root_link;
    }

    public void setRoot_link(String root_link) {
        this.root_link = root_link;
    }

   
}
