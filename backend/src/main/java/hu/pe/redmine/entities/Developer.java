package hu.pe.redmine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="developers")
public class Developer extends BaseEntity {
    private String name;
    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "developers", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Project> projects;

}
