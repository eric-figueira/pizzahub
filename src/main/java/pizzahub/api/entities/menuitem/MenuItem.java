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

import jakarta.validation.constraints.*;
import lombok.*;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.menuitem.data.MenuItemResponse;

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

    @DecimalMin(value = "0.0", inclusive = false, message = "[Menu Item]: Price cannot be equal or lower than zero")
    @DecimalMax(value = "250.0", inclusive = true, message = "[Menu Item]: Price cannot be grater than 250.00")
    private BigDecimal price;

    @NotNull(message = "[Menu Item]: Name cannot be null")
    @NotBlank(message = "[Menu Item]: Name cannot be empty")
    @Size(min = 5, max = 35, message = "[Menu Item]: Menu Item name must contain at least 5 and a max of 35 characters")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "menu_item_ingredients",
        joinColumns = @JoinColumn(name = "menu_item_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @NotEmpty(message = "[Menu Item]: Ingredients list cannot be empty")
    @NotNull(message = "[Menu Item]: Ingredients list cannot be null")
    private List<Ingredient> ingredients;

    public void setName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty() || itemName.length() < 5 || itemName.length() > 35) {
            throw new IllegalArgumentException("Menu item's name must be between 5 and 35 characters and cannot be null or empty.");
        }
        this.name = itemName;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.signum() <= 0 || price.compareTo(new BigDecimal("250.0")) > 0) {
            throw new IllegalArgumentException("Menu item's price must be greater than zero and less than or equal to 250.");
        }
        this.price = price;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("Menu item's ingredients list cannot be null or empty.");
        }
        this.ingredients = ingredients;
    }

    public MenuItemResponse convertToResponseDTO() {
        return new MenuItemResponse(
            this.getId(),
            this.getPrice(),
            this.getName(),
            this.getIngredients().stream().collect(Collectors.toMap(Ingredient::getId, Ingredient::getName))
        );
    }
}
