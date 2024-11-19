package co.edu.uptc.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonModel {    
    private Long id;
    private String name;
    private String lastName;
    private LocalDate birthday;
    private Genders gender;
    private boolean isvisible;

    public enum Genders {
        MALE,
        FEMALE        
    }
}
