package com.game.service;


import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerCriteriaRepository;
import com.game.repository.PlayerRepository;
import com.game.utils.PlayerValidator;

import com.game.utils.UpdatedPlayerValidator;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final PlayerValidator playerValidator;

    private  final UpdatedPlayerValidator updatedPlayerValidator;

    private final PlayerCriteriaRepository playerCriteriaRepository;


    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerValidator playerValidator,
                             UpdatedPlayerValidator updatedPlayerValidator, PlayerCriteriaRepository playerCriteriaRepository) {
        this.playerRepository = playerRepository;
        this.playerValidator = playerValidator;
        this.updatedPlayerValidator = updatedPlayerValidator;
        this.playerCriteriaRepository = playerCriteriaRepository;
    }

    @Override
    public List<Player> findAllWithFilterByOrder(String name, String title, Race race,
                                                 Profession profession, Long after,
                                                 Long before, Boolean banned, Integer minExp,
                                                 Integer maxExp, Integer minLevel, Integer maxLevel,
                                                 PlayerOrder order, Integer pageNumber, Integer pageSize) {

        List<Player> players = playerCriteriaRepository.findAllWithFilter(name, title, race, profession,
                after, before, banned, minExp, maxExp, minLevel, maxLevel);

        PagedListHolder<Player> playerPagedListHolder = new PagedListHolder<>(players,
                new MutableSortDefinition(order.getFieldName(), true, true));
        playerPagedListHolder.setPage(pageNumber);
        playerPagedListHolder.setPageSize(pageSize);

        return playerPagedListHolder.getPageList();
    }

    @Override
    public Integer getPlayersCount(String name, String title,
                                   Race race, Profession profession,
                                   Long after, Long before, Boolean banned,
                                   Integer minExp, Integer maxExp, Integer minLevel,
                                   Integer maxLevel) {
        return playerCriteriaRepository.findAllWithFilter(name, title,
                race, profession,
                after, before, banned,
                minExp, maxExp, minLevel,
                maxLevel).size();
    }


    @Override
    public Player findById(Long id) {
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            return player.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Player createPlayer(Player player, BindingResult bindingResult) {
        playerValidator.validate(player, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (player.getBanned() == null) {
            player.setBanned(false);
        }
            calculateExpAndLevel(player);
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player updatedPlayer, BindingResult bindingResult) {
        Player player = findById(id);

        if (playerIsEmpty(updatedPlayer)) {
            return player;
        }

        updatedPlayerValidator.validate(updatedPlayer, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(updatedPlayer.getName() != null) {
            player.setName(updatedPlayer.getName());
        }
        if(updatedPlayer.getTitle() != null) {
            player.setTitle(updatedPlayer.getTitle());
        }
        if(updatedPlayer.getRace() != null) {
            player.setRace(updatedPlayer.getRace());
        }
        if(updatedPlayer.getProfession() != null) {
            player.setProfession(updatedPlayer.getProfession());
        }
        if(updatedPlayer.getBirthday() != null) {
            player.setBirthday(updatedPlayer.getBirthday());
        }
        if(updatedPlayer.getBanned() != null) {
            player.setBanned(updatedPlayer.getBanned());
        }
        if(updatedPlayer.getExperience() != null) {
            player.setExperience(updatedPlayer.getExperience());
        }

        calculateExpAndLevel(player);

        return playerRepository.save(player);
    }

    @Override
    public boolean playerIsEmpty(Player updatedPlayer) {
            return Stream.of(updatedPlayer.getId(), updatedPlayer.getName(), updatedPlayer.getTitle(),
                            updatedPlayer.getRace(), updatedPlayer.getProfession(), updatedPlayer.getLevel(),
                            updatedPlayer.getExperience(), updatedPlayer.getBanned(),
                            updatedPlayer.getUntilNextLevel(), updatedPlayer.getBirthday())
                    .allMatch(Objects::isNull);
    }


    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(findById(id).getId());
    }


    private Player calculateExpAndLevel(Player player) {
        int currentLevel = (int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
        int untilNextLevel = (50 * (currentLevel + 1) * (currentLevel + 2)) - player.getExperience();
        player.setLevel(currentLevel);
        player.setUntilNextLevel(untilNextLevel);
        return player;
    }
}

