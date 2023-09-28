package com.example.HelpingHands.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;


@EqualsAndHashCode(callSuper = true,exclude = "events")
@DiscriminatorValue("organization")
@Data
@NoArgsConstructor
@Entity
@Table(name = "organizations")
public class Organization extends UserEntity {
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length=30)
    private String website;

    @Column(length=40)
    private String type;

    @Column(length=20)
    private String founder;

    @Column(name = "founded_at")
    private Date foundedAt;

    @JsonManagedReference(value = "organization")
    @OneToMany(mappedBy = "organization")
    private Set<Event> events;
    public Organization(String email, String password, String address, String phone, String role,String name , String description, String type, String founder, Date foundedAt) {
        super(email, password, address, phone,role,name);
        this.description = description;
        this.type = type;
        this.founder = founder;
        this.foundedAt = foundedAt;
    }
}
