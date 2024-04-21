package pizzahub.api.entities.menuitem;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pizzahub.api.entities.Ingredient;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private String name;

    @ManyToOne
    @JoinColumn(name="id")
    private Set<Ingredient> ingredients;

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

    public MenuItem(MenuItemDTO data) {
        this.name = data.name();
        this.price = data.price();
    }
}
