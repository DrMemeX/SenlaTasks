package features.customers;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 12L;

    private long id;
    private String name;
    private String phone;
    private String email;
    private String address;

    public Customer(String name,
                    String phone,
                    String email,
                    String address) {

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Customer(long id,
                    String name,
                    String phone,
                    String email,
                    String address) {
        this.id = id;

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == 0;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format(
                "Клиент #%d: %s, тел: %s, email: %s, адрес: %s",
                id, name, phone, email, address
        );
    }
}
