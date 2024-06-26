import java.util.*;

/**
 * 2024.06.26 김재근 : 정상동작확인(총 6인, 2인팀, 2일 기준)
 * -> 하드코딩된부분, 기타 배열, 리스트크기 등 범용성있게 수정필요
 * -> 3일이상 혹은 3인이상 가능하게 수정필요
 * -> 출력부분 직관적으로 수정필요
 */

/*
 * 기능
 * ------------------------------
 * 메인, 대상 플레이어 선택
 * 대상 플레이어가 이미 선택된 상태인지 확인 후 진행 혹은 재선택
 * 팀 목록에 메인과 대상 플레이어를 상호등록
 * 팀 등록 후 이미 선택된 플레이어 리스트에 메인 플레이어 추가
 * 전체 수행 후 플레이어별 매칭된 팀 리스트와 날짜 출력
 * */

public class LunchMate {
    Map<String, String[]> lunchTeam = new HashMap<>();
    String[] players = {"A", "B", "C", "D", "E", "F"};
    List<String> pickedPlayers;

    public static void main(String[] args) {
        LunchMate lunch = new LunchMate();
        lunch.mixLunchTeam(1);
        lunch.mixLunchTeam(2);
    }

    public void mixLunchTeam(int day) {
        //점심메이트 선택 메서드
        //선택리스트 날짜별 초기화
        pickedPlayers = new ArrayList<>(6);

        for (int i = 0; i < players.length; i++) {
            //플레이어 선택
            Team team = pickPlayer(i, day);
            //선택된목록에 있는경우 null리턴, 반복 스킵
            if (team == null) {
                continue;
            }
            //정상선택된경우 메인, 대상 플레이어 분리
            String me = team.getMe();
            String who = team.getWho();

            //팀목록에 추가
            setTeam(me, who, day);
        }

        //출력
        printList(day);
    }

    public int rNum(int length) {
        //난수생성메서드.
        Random r = new Random();
        return r.nextInt(length);
    }

    public void setTeam(String me, String who, int day) {
        //메인 <- 대상 등록
        String[] myMatch = lunchTeam.get(me);
        //최초 팀 등록시 초기형태로 등록
        if (myMatch == null) {
            myMatch = new String[]{"1", "2"};
        }
        myMatch[day - 1] = who;
        lunchTeam.put(me, myMatch);

        //대상 <- 메인 등록
        String[] whoMatch = lunchTeam.get(who);
        //최초 팀 등록시 초기형태로 등록
        if (whoMatch == null) {
            whoMatch = new String[]{"1", "2"};
        }
        whoMatch[day - 1] = me;
        lunchTeam.put(who, whoMatch);

        //선택된 리스트에 메인, 대상 모두 추가
        pickedPlayers.add(me);
        pickedPlayers.add(who);
    }

    public Team pickPlayer(int index, int day) {
        //메인, 대상 플레이어 선택
        boolean p = true;
        String me;
        String who = "";

        me = players[index];

        //내가 이미 선택리스트에 있다면 null 리턴
        if (pickedPlayers.contains(me)) {
            return null;
        }

        while (p) {
            who = players[rNum(players.length)];
            //대상이 나와 다르고, 선택된 플레이어 리스트에 없고, 1일차와 같은 팀이 아니라면 반복 종료
            //선택된 플레이어 리스트에 대상을 추가
            if (!(me.equals(who))) {
                if (!pickedPlayers.contains(who)) {
                    //2일차부터 검증
                    if (day > 1) {
                        //메인 플레이어의 1일차 팀과 현재 선택된 대상이 일치하지않으면 반복종료, 최종선택
                        if (!lunchTeam.get(players[index])[day - 2].equals(who)) {
                            p = false;
                        }
                    } else {
                        p = false;
                    }
                }
            }
        }

        Team team = new Team();
        team.setMe(me);
        team.setWho(who);

        return team;
    }

    public void printList(int day) {
        for (Map.Entry<String, String[]> entry : lunchTeam.entrySet()) {
            String me = entry.getKey();
            String[] myTeam = entry.getValue();
            System.out.println(me + ", " + Arrays.toString(myTeam) + " - " + day + "일차");
        }
        System.out.println();
    }
}

class Team {
    private String me;
    private String who;

    public void setMe(String me) {
        this.me = me;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getMe() {
        return me;
    }

    public String getWho() {
        return who;
    }
}
