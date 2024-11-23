package sfera.tsm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import sfera.tsm.entity.enums.Priority;
import sfera.tsm.entity.enums.Status;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Builder
public class Task implements Serializable {
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
    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;

}
