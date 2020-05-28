package com.KartonDCP.Server.DatabaseWorker.Models;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

//CAN BY JPA
@DatabaseTable(tableName = "users")
public class UserEntity implements Serializable {

    public UserEntity(UUID userToken, String name, String surname, String pwd, String phone){
        this.password = pwd;
        this.userToken = userToken;
        this.name = name;
        this.phoneNum = phone;
        this.surname = surname;
    }
    public UserEntity(){ }

    @DatabaseField(generatedId = true, unique = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true, dataType = DataType.UUID, columnName = "user_token")
    private UUID userToken;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String surname;

    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(canBeNull = false, unique = true)
    private String phoneNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUserToken() {
        return userToken;
    }

    public void setUserToken(UUID userToken) {
        this.userToken = userToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
