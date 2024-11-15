package sfera.tsm.entity;

import jakarta.persistence.*;
import lombok.*;
import sfera.tsm.entity.enums.Priority;
import sfera.tsm.entity.enums.Status;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @ManyToOne
    private User author;

}
