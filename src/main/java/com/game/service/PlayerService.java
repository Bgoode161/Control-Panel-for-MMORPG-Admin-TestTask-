package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


public interface PlayerService {

    public List<Player> findAllWithFilterByOrder(String name, String title, Race race, Profession profession,
                                          Long after, Long before, Boolean banned, Integer minExp, Integer maxExp,
                                          Integer minLevel, Integer maxLevel, PlayerOrder order,
                                                 Integer pageNumber,  Integer pageSize) ;
    Integer getPlayersCount(String name, String title, Race race, Profession profession,
                            Long after, Long before, Boolean banned, Integer minExp, Integer maxExp,
                            Integer minLevel, Integer maxLevel);

    Player findById(Long id);

    Player createPlayer(Player player, BindingResult bindingResult);

    Player updatePlayer(Long id, Player updatedPlayer, BindingResult bindingResult);

    public boolean playerIsEmpty (Player updatedPlayer);




    void deletePlayer(Long id);
}
