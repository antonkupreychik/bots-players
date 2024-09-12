package com.game.model.dto;

import com.game.model.enums.Profession;
import com.game.model.enums.Race;

public class PlayerDTO {
    public Long id;
    public String name;
    public String title;
    public Race race;
    public Profession profession;
    public Long birthday;
    public Boolean banned;
    public Integer experience;
    public Integer level;
    public Integer untilNextLevel;
}