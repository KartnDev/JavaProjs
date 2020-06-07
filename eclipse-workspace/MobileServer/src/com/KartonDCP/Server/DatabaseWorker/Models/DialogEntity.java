package com.KartonDCP.Server.DatabaseWorker.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Collection;
import java.util.UUID;

public class DialogEntity {

    @DatabaseField(generatedId = true, unique = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID)
    private UUID dialogUUID;

    @DatabaseField(canBeNull = false)
    private String dialogName;

    @ForeignCollectionField
    private Collection<MessageEntity> messages;
}
