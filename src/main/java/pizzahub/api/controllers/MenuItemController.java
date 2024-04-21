package pizzahub.api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.menuitem.MenuItemDTO;
import pizzahub.api.repositories.MenuItemRepository;

@RestController
@RequestMapping(value = "/menuitems")
public class MenuItemController {

    /*
     * To Do:
     * - Pagination
     * - Order by price ?
     * - Post (must have permission)
     * - Delete (must have permission)
     * - Update (must have permission)
    */

    @Autowired
    private MenuItemRepository repository;

    @GetMapping
    public ResponseEntity<List<MenuItem>> fetchMenuItems(
        @RequestParam(value = "page", defaultValue = "1") short page,
        @RequestParam(value = "perPage", defaultValue = "30") short perPage
    ) {
        List<MenuItem> all = this.repository.findAll();

        short start = (short) ((page - 1) * perPage);
        short end   = (short) (page * perPage);

        if (start >= all.size() || end >= all.size()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ArrayList<MenuItem>());
        }

        List<MenuItem> paginated = all.subList((page - 1) * perPage, page * perPage);

        return ResponseEntity.ok(paginated);
    }

    @GetMapping("/{id}")
    public Optional<MenuItem> fetchMenuItemById(@PathVariable("id") Long menuItemId) {
        return this.repository.findById(menuItemId);
    }

    @PostMapping
    public ResponseEntity<Long> createMenuItem(@RequestBody @Valid MenuItemDTO body) {
        // Must have permission
        MenuItem newMenuItem = new MenuItem(body);

        this.repository.save(newMenuItem);
        return ResponseEntity.ok(newMenuItem.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMenuItem(@PathVariable("id") Long menuItemId) {
        // Must have permission
        this.repository.deleteById(menuItemId);
        return ResponseEntity.ok().build();
    }
}
