package pizzahub.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pizzahub.api.entities.MenuItem;
import pizzahub.api.repositories.MenuItemRepository;

@RestController
@RequestMapping(value = "/menuitem")
public class MenuItemController {
    @Autowired
    private MenuItemRepository repository;

    @GetMapping
    public List<MenuItem> fetchMenuItems() {
        return this.repository.findAll();
    }
}
