package ci.koodysgroup.domains.entities;

import ci.koodysgroup.domains.types.LocalizedText;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "media_types")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MediaType extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "libelle", columnDefinition = "jsonb")
    private LocalizedText libelle;

    @OneToMany(mappedBy = "mediaType", cascade = CascadeType.ALL)
    private List<Content> contents = new ArrayList<>();

    public MediaType(LocalizedText libelle)
    {
        this.libelle = libelle;
    }

}
