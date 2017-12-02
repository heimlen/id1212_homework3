package se.kth.id1212.heimlen.homework3.dto;

import java.io.Serializable;

/**
 * Created by heimlen on 2017-11-29.
 */
public interface FileDTO {

    public String getName();

    public long getSize();

    public AccountDTO getOwner();

    public boolean isPublicAccess();

    public boolean isPublicWrite();

    public boolean isReadPermission();
}
