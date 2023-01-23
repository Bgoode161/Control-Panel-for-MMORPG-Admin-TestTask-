package com.game.utils;

import com.game.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.util.Calendar;
import java.util.Date;

@Component
public class UpdatedPlayerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;
        if (player.getId() != null && player.getId() < 1) {
            errors.reject("", "Id is not valid");
        }
        if (player.getName() != null && (player.getName().isEmpty() || player.getName().length() > 12)) {
            errors.reject("", "Name is not valid");
        }
        if (player.getTitle() != null && (player.getTitle().isEmpty() || player.getTitle().length() > 30)) {
            errors.reject("", "Title is not valid");
        }
        if (player.getBirthday() != null && (player.getBirthday().getTime() < 0
                || player.getBirthday().before(new Date(100, Calendar.JANUARY, 1))
                || player.getBirthday().after(new Date(1100, Calendar.JANUARY, 1)))) {
            errors.reject("Birthday is not valid");
        }
        if (player.getExperience() != null && (player.getExperience() > 10000000 || player.getExperience() < 0)) {
            errors.reject(" ", "Experience value is not valid");
        }

    }
}
