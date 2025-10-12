package ci.koodysgroup.domains.entities;

import ci.koodysgroup.domains.types.LocalizedText;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Category extends AbstractDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "libelle", columnDefinition = "jsonb")
    private LocalizedText libelle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description", columnDefinition = "jsonb")
    private LocalizedText description;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Movie> movies = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Serie> series = new ArrayList<>();

    public Category(LocalizedText libelle, LocalizedText description)
    {
        this.libelle = libelle;
        this.description = description;
    }
}
