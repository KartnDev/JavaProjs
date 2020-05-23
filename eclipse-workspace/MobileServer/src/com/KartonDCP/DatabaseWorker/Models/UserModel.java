package com.KartonDCP.DatabaseWorker.Models;

import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;


@Table(appliesTo = "users")
public class UserModel implements Serializable {
    @Id
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "token", unique = true)
    private UUID user_token;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_num", nullable = false)
    private String phoneNum;

}
