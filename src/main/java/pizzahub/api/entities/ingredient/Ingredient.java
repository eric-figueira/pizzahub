package pizzahub.api.entities.ingredient;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import pizzahub.api.entities.menuitem.MenuItem;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "ingredients")
    private List<MenuItem> menuItems = new ArrayList<>();

    public void setId(Long id) throws Exception {
        if (id.toString().isEmpty()) {
            throw new IllegalArgumentException("[Ingredient]: Id cannot be null or empty");
        }
        this.id = id;
    }

    public void setName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[Ingredient]: Name cannot be null or empty");
        }
        this.name = name;
    }

    public Ingredient(IngredientRequestDTO data) throws Exception {
        try {
            this.setName(data.name());
        } catch (Exception error) {
            throw error;
        }
    }
}
