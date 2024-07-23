package org.groom.io.how_lunch;

import java.util.List;

public class LunchTeam {
    private int day;
    private List<String[]> teams;

    public LunchTeam(int day, List<String[]> teams) {
        this.day = day;
        this.teams = teams;
    }

    public int getDay() {
        return day;
    }

    public List<String[]> getTeams() {
        return teams;
    }

    public void setTeam(int day, String[] team) {
        teams.set(day - 1, team);
    }
}