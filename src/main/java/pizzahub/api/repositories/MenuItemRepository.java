package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzahub.api.entities.MenuItem;

public interface MenuItemRepository extends JpaRepository <MenuItem, Long> {}
