package se.kth.id1212.heimlen.homework3.model;

import org.hibernate.annotations.GenericGenerator;
import se.kth.id1212.heimlen.homework3.dto.AccountDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by heimlen on 2017-11-28.
 */
@Entity (name = "Account")
public class Account implements AccountDTO{
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner")
    private Collection<File> files = new ArrayList<>();

    public Account(){}

    public Account(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public long getId() {
        return id;
    }

}
