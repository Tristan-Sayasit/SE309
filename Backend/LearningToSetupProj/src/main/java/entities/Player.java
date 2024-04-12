package entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long User_Id;
    @Nonnull
    private String First_name;
    @Nonnull
    private String Last_name;
    @Nonnull
    private String User_Name;
    @Nonnull
    private String Email;
    @Nonnull
    private String Password;
}
