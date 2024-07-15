import java.util.*;

/**
 * 2024.06.26 김재근 : 정상동작확인(총 6인, 2인팀, 2일 기준)
 * -> 하드코딩된부분, 기타 배열, 리스트크기 등 범용성있게 수정필요
 * -> 3일이상 혹은 3인이상 가능하게 수정필요
 * -> 출력부분 직관적으로 수정필요
 * 2024.06.27 김재근 : 입력으로 인원설정 가능하게 수정
 * -> 최대인원제한, 홀수일경우 예외처리 필요
 * 2024.07.15 김재근 : 범용 가능하게 최종수정
 */


public class LunchMate {
    int playerNumber;
    int days;
    int teamSize;
    Map<String, List<String[]>> lunchTeam = new HashMap<>();
    String[] players;
    Map<String, Set<String>> previousTeams = new HashMap<>();

    public LunchMate(int playerNumber, int days, int teamSize){
        this.playerNumber = playerNumber;
        this.days = days;
        this.teamSize = teamSize;
        players = new String[playerNumber];
        initMember();
    }

    void initMember(){
        for(int i = 0; i < players.length; i++){
            players[i] = String.valueOf((char)('A' + i));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("플레이어 수를 입력하세요: ");
        int inputNumber = scanner.nextInt();

        System.out.print("일수를 입력하세요: ");
        int days = scanner.nextInt();

        System.out.print("팀원 수를 입력하세요: ");
        int teamSize = scanner.nextInt();

        LunchMate lunch = new LunchMate(inputNumber, days, teamSize);
        for (int i = 1; i <= days; i++) {
            lunch.mixLunchTeam(i);
        }
        scanner.close();
    }

    public void mixLunchTeam(int day) {
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
                lunchTeam.computeIfAbsent(member, k -> new ArrayList<>(Collections.nCopies(days, null)));
                lunchTeam.get(member).set(day - 1, team.toArray(new String[0]));
            }
        }

        // previousTeams에 추가
        for (List<String> team : teams) {
            for (String member : team) {
                Set<String> prevTeamMembers = previousTeams.computeIfAbsent(member, k -> new HashSet<>());
                prevTeamMembers.addAll(team);
                prevTeamMembers.remove(member);
            }
        }

        // 당일 팀 출력
        printList(day);
    }

    public int rNum(int length) {
        Random r = new Random();
        return r.nextInt(length);
    }

    public void printList(int day) {
        System.out.println(day + "일차 팀 목록:");
        for (Map.Entry<String, List<String[]>> entry : lunchTeam.entrySet()) {
            String me = entry.getKey();
            String[] myTeam = entry.getValue().get(day - 1);
            System.out.println(me + " - " + Arrays.toString(myTeam));
        }
        System.out.println();
    }
}