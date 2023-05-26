/**
 * Data collected for each player
 */
public class Player {

    // Username
    private String username;
    // Money - float used in case of floating point error
    private float money;
    // Correct
    private int correct;
    // Incorrect
    private int incorrect;

    /*
    Create Player object
     */
    public Player(String username, int money, int correct, int incorrect) {
        this.username = username;
        this.money = money;
        this.correct = correct;
        this.incorrect = incorrect;
    }

    /**
     * Get amount incorrect
     * @return incorrect
     */
    public int getIncorrect() {
        return incorrect;
    }

    /**
     * Set amount correct
     * @param incorrect amount
     */
    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    /**
     * Get amount correct
     * @return correct
     */
    public int getCorrect() {
        return correct;
    }

    /**
     * Set amount correct
     * @param correct correct
     */
    public void setCorrect(int correct) {
        this.correct = correct;
    }

    /**
     * Get money amount
     * @return money
     */
    public float getMoney() {
        return money;
    }

    /**
     * Set money amount
     * @param money money
     */
    public void setMoney(float money) {
        this.money = money;
    }

    /**
     * Get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * ToString method for debugging.
     * @return username
     */
    @Override
    public String toString() {
        return getUsername();
    }
}
