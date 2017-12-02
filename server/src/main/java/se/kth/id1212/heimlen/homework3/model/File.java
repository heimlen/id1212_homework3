package se.kth.id1212.heimlen.homework3.model;

import org.hibernate.annotations.GenericGenerator;
import se.kth.id1212.heimlen.homework3.dto.FileDTO;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The representation of a file in the filesystem
 * */
@Entity
public class File implements FileDTO, Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private long size;
    @Column(nullable = false, length = 500)
    private Account owner;
    @Column(nullable = false)
    private boolean publicAccess;
    @Column(nullable = false)
    private boolean writePermission;


    @Column(nullable = false)
    private boolean readPermission;

    public File() {
    }

    public File(long id, String name, long size, Account owner, boolean publicAccess, boolean writePermission, boolean readPermission) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.owner = owner;
        this.publicAccess = publicAccess;
        this.writePermission = writePermission;
        this.readPermission = readPermission;
    }

    public File(String name, long size, Account owner, boolean publicAccess, boolean writePermission, boolean readPermission) {
        this.name = name;
        this.size = size;
        this.owner = owner;
        this.publicAccess = publicAccess;
        this.writePermission = writePermission;
        this.readPermission = readPermission;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public Account getOwner() {
        return owner;
    }

    @Override
    public boolean isPublicAccess() {
        return publicAccess;
    }

    @Override
    public boolean isPublicWrite() {
        return writePermission;
    }

    @Override
    public boolean isReadPermission() {
        return readPermission;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public void setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

    public void setReadPermission(boolean readPermission) {
        this.readPermission = readPermission;
    }
}