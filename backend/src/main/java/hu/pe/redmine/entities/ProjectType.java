package hu.pe.redmine.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="project_types")
public class ProjectType extends BaseEntity{
    private String name;
}
