package com.KartonDCP.Server.DatabaseWorker.Models;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.UUID;

@DatabaseTable(tableName = "chat")
public class ChatEntity {

    @DatabaseField(generatedId = true, unique = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID chatUUID;

    @DatabaseField(canBeNull = false)
    private String chatName;


    @ForeignCollectionField
    private Collection<DialogEntity> chatUsers;

    @ForeignCollectionField
    private Collection<MessageEntity> messages;

}
