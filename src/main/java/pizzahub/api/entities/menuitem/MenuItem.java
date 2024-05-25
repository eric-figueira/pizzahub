package pizzahub.api.entities.menuitem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.menuitem.data.CreateMenuItemRequestDTO;
import pizzahub.api.entities.menuitem.data.MenuItemResponseDTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "menu_item")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private String name;

    @ManyToMany
    @JoinTable(
        name = "menu_item_ingredients",
        joinColumns = @JoinColumn(name = "menu_item_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    public void setId(Long id) throws Exception {
        if (id.toString().isEmpty()) {
            throw new IllegalArgumentException("[Menu Item]: Id cannot be null or empty");
        }
        this.id = id;
    }

    public void setPrice(BigDecimal price) throws Exception {
        if (price.signum() == -1) {
            throw new IllegalArgumentException("[Menu Item]: Price cannot be lower than zero");
        }
        this.price = price;
    }

    public void setName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[Menu Item]: Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setIngredients(List<Ingredient> ingredients) throws Exception {
        if (ingredients == null || ingredients.size() == 0) {
            throw new IllegalArgumentException("[Menu Item]: Ingredients list cannot be null or empty");
        }
        this.ingredients = ingredients;
    }

    public MenuItem(CreateMenuItemRequestDTO data) throws Exception {
        this.setName(data.name());
        this.setPrice(data.price());
    }

    public MenuItemResponseDTO convertToResponseDTO() {
        return new MenuItemResponseDTO(
            this.getId(),
            this.getPrice(),
            this.getName(),
            this.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())
        );
    }
}
