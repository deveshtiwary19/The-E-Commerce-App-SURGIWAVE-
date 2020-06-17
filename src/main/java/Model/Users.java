package Model;

public class Users {

    private String Name, Phone, Password, Image, Adress;

    public Users() {

    }

    public Users(String name, String phone, String password, String image, String adress) {
        Name = name;
        Phone = phone;
        Password = password;
        Image = image;
        Adress = adress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }
}