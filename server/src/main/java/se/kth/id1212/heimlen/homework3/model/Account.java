package se.kth.id1212.heimlen.homework3.model;

import org.hibernate.annotations.GenericGenerator;
import se.kth.id1212.heimlen.homework3.dto.CredentialDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Representation of an Account.
 */

@Entity (name = "Account")
public class Account implements se.kth.id1212.heimlen.homework3.dto.AccountDTO, Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Collection<File> files = new ArrayList<>();

    public Account(){}

    public Account(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Account(CredentialDTO credentials) {
        this.username = credentials.getUsername();
        this.password = credentials.getPassword();
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

    public Collection<File> getFiles() {
        return files;
    }

    public void setFiles(Collection<File> files) {
        this.files = files;
    }
}
