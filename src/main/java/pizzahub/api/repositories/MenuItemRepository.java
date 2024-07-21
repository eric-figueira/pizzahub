package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.menuitem.MenuItem;

import java.util.Optional;

public interface MenuItemRepository extends JpaRepository <MenuItem, Long> {
    Optional<MenuItem> findBySlug(String slug);
}
