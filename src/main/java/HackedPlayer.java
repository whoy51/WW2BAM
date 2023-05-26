public class HackedPlayer extends Player{
    /**
     * Extension of player w/ some extra properties...
     * @param username #secretplayer
     * @param money hacked
     * @param correct hacked
     * @param incorrect hacked
     */


    public HackedPlayer(String username, int money, int correct, int incorrect) {
        super(username, money + 10000, correct + 1000, incorrect - 1000);
    }


    @Override
    public String toString() {
        return "You have found the secret player! Your username is: " +  this.getUsername();
    }
}
