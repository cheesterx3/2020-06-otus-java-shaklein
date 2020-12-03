package ru.otus.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import ru.otus.dto.UserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressDataSet address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PhoneDataSet> phones = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public static User fromDto(UserDto userDto) {
        val user = new User(userDto.getName());
        if (!userDto.getAddress().isEmpty())
            user.setAddress(new AddressDataSet(userDto.getAddress()));
        if (nonNull(userDto.getPhones()))
            Stream.of(userDto.getPhones().split(","))
                    .filter(Predicate.not(String::isEmpty))
                    .map(String::trim)
                    .map(PhoneDataSet::new)
                    .forEach(user::addPhone);
        return user;
    }

    public void addPhone(PhoneDataSet phone) {
        this.phones.add(phone);
        phone.setUser(this);
    }

    public User copy() {
        val user = new User(name);
        user.setId(id);
        if (nonNull(address))
            user.setAddress(address.copy());
        phones.forEach(phone -> user.addPhone(phone.copy()));
        return user;
    }

    public String getAddressInfo() {
        if (isNull(address))
            return "";
        return address.getStreet();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
