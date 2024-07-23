package org.groom.io.how_lunch;


import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int playerNumber = 31;
        int days = 5;
        int teamSize = 3;

        LunchMate lunchMate = new LunchMate(playerNumber, days, teamSize);
        List<LunchTeam> allTeams = lunchMate.generateTeams();
        printTeams(allTeams);
    }

    public static void printTeams(List<LunchTeam> allTeams) {
        for (LunchTeam dayTeams : allTeams) {
            int day = dayTeams.getDay();
            List<String[]> teams = dayTeams.getTeams();
            System.out.println(day + "일차 팀 목록:");
            for (String[] team : teams) {
                System.out.println(Arrays.toString(team));
            }
            System.out.println();
        }
    }
}