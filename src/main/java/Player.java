public class Player {

    private String username;
    private float money;
    private int correct;
    private int incorrect;

    public Player(String username, int money, int correct, int incorrect) {
        this.username = username;
        this.money = money;
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
