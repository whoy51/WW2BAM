public class HackedPlayer extends Player{


    public HackedPlayer(String username, int money, int correct, int incorrect) {
        super(username + " #secretplayer", money + 10000, correct + 1000, incorrect - 1000);
    }


    @Override
    public String toString() {
        return "You have found the secret player!";
    }
}
