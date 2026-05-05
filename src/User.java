

public class User {
    private String username;
    private String password;
    private String personid;
    private long phonenumber;

    public User(){

    }

    public User(String username,String password,String personid,long phonenumber){
        this.username=username;
        this.password=password;
        this.personid=personid;
        this.phonenumber=phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(long phonenumber) {
        this.phonenumber = phonenumber;
    }


}
