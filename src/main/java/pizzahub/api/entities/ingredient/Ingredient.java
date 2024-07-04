package pizzahub.api.entities.ingredient;

import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pizzahub.api.entities.ingredient.data.IngredientResponse;
import pizzahub.api.entities.menuitem.MenuItem;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() < 3 || name.length() > 35) {
            throw new IllegalArgumentException("Ingredient's name must be between 3 and 35 characters and cannot be null or empty");
        }
        this.name = name;
    }
}
