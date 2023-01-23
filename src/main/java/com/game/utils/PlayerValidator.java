package com.game.utils;

import com.game.entity.Player;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class PlayerValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;

        if (player.getName() == null || player.getName().length() > 12 || player.getName().isEmpty()) {
            errors.reject("", "Name is not valid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (player.getTitle() == null || player.getTitle().isEmpty() || player.getTitle().length() > 30) {
            errors.reject("", "Title is not valid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (player.getRace() == null) {
            errors.reject("", "Race value required");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (player.getProfession() == null) {
            errors.reject("", "Profession value required");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (player.getBirthday() == null || player.getBirthday().getTime() < 0
                || player.getBirthday().before(new Date(100, Calendar.FEBRUARY, 1))
                || player.getBirthday().after(new Date(1100, Calendar.JANUARY, 1))) {
            errors.reject("Birthday is not valid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (player.getExperience() > 10000000 || player.getExperience() < 0 || player.getExperience() == null) {
            errors.reject(" ", "Experience value is not valid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
