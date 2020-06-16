package com.KartonDCP.Server.DatabaseWorker.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;


public class DialogEntity implements Serializable {

    public DialogEntity(){

    }

    public DialogEntity(UUID dialogUUID, UUID user1, UUID user2) {
        this.dialogUUID = dialogUUID;
        this.user1Self = user1;
        this.user2 = user2;
    }

    private static final long serialVersionUID = 1L;

    @DatabaseField(foreign=true)
    private UserEntity byUser;

    @DatabaseField(generatedId = true, unique = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID dialogUUID;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID user1Self;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID user2;

    private Collection<MessageEntity> messages;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getDialogUUID() {
        return dialogUUID;
    }

    public void setDialogUUID(UUID dialogUUID) {
        this.dialogUUID = dialogUUID;
    }

    public UUID getUser1Self() {
        return user1Self;
    }

    public void setUser1Self(UUID user1Self) {
        this.user1Self = user1Self;
    }

    public UUID getUser2() {
        return user2;
    }

    public void setUser2(UUID user2) {
        this.user2 = user2;
    }

    public Collection<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(Collection<MessageEntity> messages) {
        this.messages = messages;
    }
}
