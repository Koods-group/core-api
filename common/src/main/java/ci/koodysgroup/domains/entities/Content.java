package ci.koodysgroup.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contents")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Content extends AbstractDateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(name = "media_type_id")
    private MediaType mediaType;

    @Column(name = "poster_url")
    private String posterUrl;

    @OneToOne(mappedBy = "content" , cascade = CascadeType.ALL)
    private Movie movie;

    @OneToOne(mappedBy = "content" , cascade = CascadeType.ALL)
    private Serie serie;
}
