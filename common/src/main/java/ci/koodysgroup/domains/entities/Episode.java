package ci.koodysgroup.domains.entities;

import ci.koodysgroup.domains.types.LocalizedText;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.UUID;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Episodes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Episode extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "title", columnDefinition = "jsonb")
    private LocalizedText title;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Column(name = "must_be_seen")
    private boolean mustBeSeen;

    @Column(name = "episode_number")
    private int episodeNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description", columnDefinition = "jsonb")
    private LocalizedText description;

    @Column(name = "duration")
    private Time duration;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "video_link" , nullable = false)
    private String videoLink;

}
