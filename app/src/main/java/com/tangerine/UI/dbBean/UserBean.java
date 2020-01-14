package com.tangerine.UI.dbBean;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.Unique;

@Entity
public class UserBean {
    @Id
    long id;
    @Unique
    @Index
    @NameInDb("USER")
    public String name;
    public UserBean(String name){
        this.name = name;
    }

    public UserBean(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
