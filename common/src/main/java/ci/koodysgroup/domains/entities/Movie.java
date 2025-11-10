package ci.koodysgroup.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @Column(name = "duration")
    private Time duration;

    @Column(name = "director")
    private String director;

    @Column(name = "video_link" , nullable = false)
    private String videoLink;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Movie(Category category, Content content, Time duration, String director , String videoLink)
    {
        this.category = category;
        this.content = content;
        this.duration = duration;
        this.director = director;
        this.videoLink = videoLink;
    }
}
