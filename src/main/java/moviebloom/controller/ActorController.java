package moviebloom.controller;

import moviebloom.domain.Actor;
import moviebloom.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/actor")
public class ActorController {
    @Autowired
    private ActorService actorService;

    @GetMapping("/{id}")
    public Actor findActor(@PathVariable Long id){
        return actorService.findOne(id);
    }

    @GetMapping
    public List<Actor> findAllActors(){
        return actorService.findAll();
    }
}
