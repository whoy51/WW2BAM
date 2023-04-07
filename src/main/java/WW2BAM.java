import java.io.IOException;

public class WW2BAM {

    public static void main(String[] args) throws IOException {
//        Game game = new Game();

        Leaderboard board = new Leaderboard();

        board.addPlayer(new Player("Wesley", 200, 20, 2));
        board.addPlayer(new Player("Luther", 400, 60, 2));
        board.addPlayer(new Player("a", 23142100, 60, 2));
        board.addPlayer(new Player("Lb", 4032940, 60, 2));
        board.addPlayer(new Player("c", 409430, 60, 2));
        board.addPlayer(new Player("d", 40412480, 60, 2));
        board.addPlayer(new Player("e", 324400, 60, 2));

        System.out.println(board.getLeaderboard());
    }



}
