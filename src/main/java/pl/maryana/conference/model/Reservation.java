package pl.maryana.conference.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lecture_id")
    private long lectureId;

    @Column(name = "thematic_path_id")
    private long thematicPathId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private Lecture lecture;
}
