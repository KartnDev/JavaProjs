package com.KartonDCP.Server.DatabaseWorker.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Collection;
import java.util.UUID;

public class DialogEntity {

    public DialogEntity(UUID dialogUUID, UUID user1, UUID user2) {
        this.dialogUUID = dialogUUID;
        this.user1 = user1;
        this.user2 = user2;
    }


    @DatabaseField(generatedId = true, unique = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID dialogUUID;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID user1;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID user2;


    @ForeignCollectionField
    @DatabaseField(canBeNull = true)
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

    public UUID getUser1() {
        return user1;
    }

    public void setUser1(UUID user1) {
        this.user1 = user1;
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
