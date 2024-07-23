package org.groom.io.how_lunch;

import java.util.*;

/**
 * 2024.06.26 김재근 : 정상동작확인(총 6인, 2인팀, 2일 기준)
 * -> 하드코딩된부분, 기타 배열, 리스트크기 등 범용성있게 수정필요
 * -> 3일이상 혹은 3인이상 가능하게 수정필요
 * -> 출력부분 직관적으로 수정필요
 * 2024.06.27 김재근 : 입력으로 인원설정 가능하게 수정
 * -> 최대인원제한, 홀수일경우 예외처리 필요
 * 2024.07.15 김재근 : 범용 가능하게 최종수정
 * 2024.07.22 김재근 : 라이브러리화
 */


public class LunchMate {
    int playerNumber;
    int days;
    int teamSize;
    Map<String, LunchTeam> lunchTeam = new HashMap<>();
    String[] players;
    Map<String, Set<String>> previousTeams = new HashMap<>();

    /*
    모듈 호출, 전달된 정보로 전체인원 초기화
    최대팀원수 54, a~z~A~Z, 대소문자 구분
    생성자(initMember) -> generateTeams() 호출
    */
    public LunchMate(int playerNumber, int days, int teamSize){
        if (playerNumber > 54) {
            throw new IllegalArgumentException("플레이어 수는 최대 54명 입니다.");
        }
        if (playerNumber < teamSize) {
            throw new IllegalArgumentException("팀원 수는 전체 인원 수보다 작아야 합니다.");
        }
        if (playerNumber < 1 || teamSize < 1 || days < 1) {
            throw new IllegalArgumentException("플레이어 수, 팀원 수, 일수는 1 이상이어야 합니다.");
        }
        this.playerNumber = playerNumber;
        this.days = days;
        this.teamSize = teamSize;
        players = new String[playerNumber];
        initMember();
    }

    void initMember(){
        for(int i = 0; i < players.length; i++){
            if (i < 26) {
                players[i] = String.valueOf((char)('a' + i));
            } else {
                players[i] = String.valueOf((char)('A' + (i - 26)));
            }
        }
    }

    public List<LunchTeam> generateTeams() {
        List<LunchTeam> allDaysTeams = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            allDaysTeams.add(new LunchTeam(i, mixLunchTeam(i)));
        }
        return allDaysTeams;
    }

    private List<String[]> mixLunchTeam(int day) {
        List<String> availablePlayers = new ArrayList<>(Arrays.asList(players));
        List<List<String>> teams = new ArrayList<>();

        // 팀 구성
        while (availablePlayers.size() >= teamSize) {
            List<String> team = new ArrayList<>();
            while (team.size() < teamSize) {
                String player = availablePlayers.remove(rNum(availablePlayers.size()));
                team.add(player);
            }
            teams.add(team);
        }

        // 남은 플레이어 분배
        for (String player : availablePlayers) {
            int teamIndex = rNum(teams.size());
            teams.get(teamIndex).add(player);
        }

        // 새로운 팀을 당일 점심 팀 목록에 업데이트
        for (List<String> team : teams) {
            for (String member : team) {
                lunchTeam.computeIfAbsent(member, k -> new LunchTeam(day, new ArrayList<>(Collections.nCopies(days, null))));
                lunchTeam.get(member).setTeam(day, team.toArray(new String[0]));
            }
        }

        // previousTeams에 추가
        for (List<String> team : teams) {
            for (String member : team) {
                Set<String> prevTeamMembers = previousTeams.computeIfAbsent(member, k -> new HashSet<>());
                prevTeamMembers.addAll(team);
                prevTeamMembers.remove(member); // 자기 자신을 제거
            }
        }

        // 현재 팀 구성을 String[] 배열로 변환하여 반환
        List<String[]> resultTeams = new ArrayList<>();
        for (List<String> team : teams) {
            resultTeams.add(team.toArray(new String[0]));
        }
        return resultTeams;
    }

    private int rNum(int length) {
        Random r = new Random();
        return r.nextInt(length);
    }
}