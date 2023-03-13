package hu.syscode.users.data;


import jakarta.persistence.*;

import java.util.UUID;

/**
 * Main entity of the application
 */
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String fullName;
    private String emailAddress;

    public UUID getId(){ return id; }
    public String getFullName(){ return fullName; }
    public String getEmailAddress(){ return emailAddress; }

    public void setFullName(String fullName){ this.fullName = fullName; }
    public void setEmailAddress(String emailAddress){ this.emailAddress = emailAddress; }
}
