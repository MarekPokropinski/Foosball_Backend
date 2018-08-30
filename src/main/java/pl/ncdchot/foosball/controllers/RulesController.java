package pl.ncdchot.foosball.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ncdchot.foosball.database.model.Rules;
import pl.ncdchot.foosball.services.RulesService;

import java.util.List;

@RestController
@RequestMapping("/rules")
public class RulesController {

    @Autowired
    private RulesService rulesService;

    @GetMapping("/all")
    public List<Rules> showAll() {
        return rulesService.getAll();
    }
}
