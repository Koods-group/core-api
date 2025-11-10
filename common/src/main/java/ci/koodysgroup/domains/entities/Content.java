package ci.koodysgroup.domains.entities;

import ci.koodysgroup.domains.types.LocalizedText;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "contents")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Content extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "title" , columnDefinition = "jsonb")
    private LocalizedText title;

    @Column(name = "must_be_seen")
    private boolean mustBeSeen;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description", columnDefinition = "jsonb")
    private LocalizedText description;

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

    public Content(LocalizedText title, boolean mustBeSeen, LocalizedText description, LocalDate releaseDate, MediaType mediaType, String posterUrl)
    {
        this.title = title;
        this.mustBeSeen = mustBeSeen;
        this.description = description;
        this.releaseDate = releaseDate;
        this.mediaType = mediaType;
        this.posterUrl = posterUrl;
    }
}
