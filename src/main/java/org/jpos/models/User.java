package org.jpos.models;

import javax.persistence.*;

@Entity
@Table(name="Users")
public class User {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name", nullable = false)
    String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name="password", nullable = false)
    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
