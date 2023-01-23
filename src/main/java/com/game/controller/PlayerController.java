package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {

    private PlayerServiceImpl playerService;

    @Autowired
    public PlayerController(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/rest/players")
    public ResponseEntity<List<Player>> findAllWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false, defaultValue = "ID") PlayerOrder playerOrder,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize
    ) {
        return new ResponseEntity<>(playerService.findAllWithFilterByOrder(
                name, title, race, profession,
                after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel,
                playerOrder, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/rest/players/count")

    public ResponseEntity<Integer> getPlayersCount(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) Race race,
                                                   @RequestParam(required = false) Profession profession,
                                                   @RequestParam(required = false) Long after,
                                                   @RequestParam(required = false) Long before,
                                                   @RequestParam(required = false) Boolean banned,
                                                   @RequestParam(required = false) Integer minExperience,
                                                   @RequestParam(required = false) Integer maxExperience,
                                                   @RequestParam(required = false) Integer minLevel,
                                                   @RequestParam(required = false) Integer maxLevel) {
        return new ResponseEntity<>(playerService.getPlayersCount(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel), HttpStatus.OK);
    }

    @GetMapping("/rest/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.findById(id);
    }

    @PostMapping("/rest/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player, BindingResult bindingResult) {
        return new ResponseEntity<>(playerService.createPlayer(player, bindingResult), HttpStatus.OK);
    }

    @PostMapping("/rest/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long id,
                                               @RequestBody(required = false) Player updatedPlayer,
                                               BindingResult bindingResult) {
        return new ResponseEntity<>(playerService.updatePlayer(id, updatedPlayer, bindingResult), HttpStatus.OK);
    }

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<HttpStatus> deletePlayer(@PathVariable("id") Long id) {
        playerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
