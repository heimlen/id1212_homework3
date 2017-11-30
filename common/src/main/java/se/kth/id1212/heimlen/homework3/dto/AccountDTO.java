package se.kth.id1212.heimlen.homework3.dto;

import java.io.Serializable;

/**
 * Created by heimlen on 2017-11-29.
 */
public interface AccountDTO extends Serializable {

    /**
     * Returns the username of the account.
     * @return
     */
    public String getUsername();

    /**
     * Returns the password of the account.
     * @return
     */
    public String getPassword();

    /**
     * Returns the id of the account.
     * @return
     */
    public long getId();
}
