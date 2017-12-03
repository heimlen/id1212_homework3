package se.kth.id1212.heimlen.homework3.dto;

import java.io.Serializable;

/**
 * Created by heimlen on 2017-12-02.
 */
public class AccountSocketIDDTO implements Serializable{
    private long id;

    public AccountSocketIDDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
