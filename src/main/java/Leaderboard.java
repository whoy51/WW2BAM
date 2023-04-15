import java.lang.reflect.Array;
import java.util.ArrayList;

public class Leaderboard {

    private ArrayList<Player> players;

    public Leaderboard(){
        players = new ArrayList<>();
        System.out.println("test");
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public String toString(){
        players = sortLeaderboard(players);
        StringBuilder str = new StringBuilder();
        int idx = 0;
        for (Player p : players){
            idx += 1;
            str.append("#").append(idx).append(" ").append(p.getUsername()).append(": ").append("$").append(p.getMoney()).append("\n");
        }
        return str.toString();
    }

    public String getIdx(int i){
        players = sortLeaderboard(players);
        return "#" + (i + 1) + ": " + players.get(i).getUsername() + " - $" + players.get(i).getMoney();
    }

    public boolean playerExists(String username){
        for (Player p : players){
            if (p.getUsername().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

    public Player getPlayer(String s){
        for (Player p : players){
            if (p.getUsername().equalsIgnoreCase(s)){
                return p;
            }
        }
        return null;
    }

    public int getSize(){
        return players.size();
    }
    // Use merge sort to recursively sort the scores of players
    private static ArrayList<Player> merge(ArrayList<Player> a, ArrayList<Player> b){
        ArrayList<Player> m = new ArrayList<>();
        int idxA = 0, idxB = 0;

        // Sort until one array is exhausted
        while (idxA < a.size() && idxB < b.size()) {
            // Check which element is shorter,
            // add that one to array and increase its idx
            if (a.get(idxA).getMoney() >= b.get(idxB).getMoney()) {
                m.add(a.get(idxA));
                idxA++;
            }
            else {
                m.add(b.get(idxB));
                idxB++;
            }
        }

        // Now one array is exhausted!
        while (idxA < a.size()){
            m.add(a.get(idxA));
            idxA++;
        }
        while (idxB < b.size()){
            m.add(b.get(idxB));
            idxB++;
        }

        return m;
    }

    /** Returns sorted array using recursive MergeSort algorithms */
    private static ArrayList<Player> sortLeaderboard(ArrayList<Player> players){
        if (players.size() <= 1){
            return players;
        }
        ArrayList<Player> left = subarray(players, 0, players.size() /2);
        ArrayList<Player> right = subarray(players, players.size() /2, players.size());

        // Recursively sort both halves
        left = sortLeaderboard(left);
        right = sortLeaderboard(right);
        return merge(left, right);
    }

    /** Returns elements including startIdx and excluding stopIdx */
    private static ArrayList<Player> subarray(ArrayList<Player> players, int startIdx, int stopIdx){
        int totalSize = stopIdx - startIdx;
        ArrayList<Player> res = new ArrayList<>();
        for (int i = 0; i < totalSize; i++){
            res.add(players.get(startIdx + i));
        }
        // Note: Use System.arrayCopy() if performance matters!
        // System.arraycopy(a, startIdx + 0, res, 0, res.length);
        return res;
    }
}
