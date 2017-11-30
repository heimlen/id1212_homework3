package se.kth.id1212.heimlen.homework3.model;

import org.hibernate.annotations.GenericGenerator;
import se.kth.id1212.heimlen.homework3.dto.AccountDTO;
import se.kth.id1212.heimlen.homework3.dto.FileDTO;

import javax.persistence.*;

/**
 * The representation of a file in the filesystem
 * */
@Entity
public class File implements FileDTO{
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    @ManyToOne
    private AccountDTO owner;
    @Column(nullable = false)
    private boolean publicAccess;
    @Column(nullable = false)
    private boolean writePermission;
    @Column(nullable = false)
    private boolean readPermission;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public AccountDTO getOwner() {
        return owner;
    }

    @Override
    public boolean isPublicAccess() {
        return publicAccess;
    }

    @Override
    public boolean isWritePermission() {
        return writePermission;
    }

    @Override
    public boolean isReadPermission() {
        return readPermission;
    }
}
