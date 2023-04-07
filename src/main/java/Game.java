import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Game {
    private int level;
    private GameState gameState;
    private GameType gameType;
    private boolean gameRunning = true;
    volatile boolean awaiting = true;
    String answer;
    private Leaderboard board;
    Player currentPlayer;

    public Game() throws IOException {
        this.level = 1;
        gameState = GameState.ONGOING;
        board = new Leaderboard();

        System.out.println("WW2BAM has been started!");

        init();


    }

    /**
     * All game logic
     * @throws IOException check getJSON() documentation
     */
    private void init() throws IOException {

        while (gameRunning) {
            level = 1;
            gameState = GameState.WAITING;
            awaiting = true;
            while (gameState == GameState.WAITING){
                promptHome();
                while (awaiting){
                    Thread.onSpinWait();
                }
            }
            if (!gameRunning){
                System.out.println("Game has ended!");
                break;
            }
            while (gameState == GameState.ONGOING) {
                System.out.println("Fetching questions... (if this takes longer than 5 seconds, restart)");
                JFrame frame = new JFrame();
                frame.setTitle("Please wait...");
                frame.setSize(450, 600);
                JPanel panel = new JPanel();
                frame.getContentPane().add(panel);
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JLabel label = new JLabel("Fetching questions... (if this takes longer than 5 seconds, restart)");
                panel.add(label);
                panel.setAlignmentX(Component.CENTER_ALIGNMENT);
                frame.pack();
                frame.setSize(frame.getWidth() + 100, frame.getHeight() + 20);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                ArrayList<String> question = getQuestion(level);
                String correct = question.get(1);
                for (int i = 0; i < 100; i++) {
                    question.add((int) Math.floor(Math.random() * 4) + 1, question.remove(1));
                }
                frame.dispose();
                awaiting = true;
                promptUser(question.get(0), question.get(1), question.get(2), question.get(3), question.get(4));

                while (awaiting) {
                    Thread.onSpinWait();
                }
                awaiting = true;
                if (answer.equals(correct)) {
                    level++;
                    System.out.println("Your level: " + level);
                    System.out.println("Correct! Next question");
                    awaiting = false;
                } else {
                    if (gameType != GameType.INFINITE){
                        gameState = GameState.LOST;
                    }
                    else {
                        level ++;
                    }
                    System.out.println("Incorrect!");
                    promptRes(gameState, correct);
                }
                if (level > 10) {
                    gameState = GameState.WON;
                    promptRes(gameState, "");
                    awaiting = false;
                }
                while (awaiting){
                    Thread.onSpinWait();
                }

            }
            System.out.println("Your level: " + level);
            System.out.println(gameState.toString());
        }
    }

    /**
     * Fetch information from OpenTriviaDatabase
     * @param level Used to determine the difficulty of the question
     * @return a JSON of the information provided by OpenTB
     * @throws IOException API is deprecated, or connection was lost
     */
    private static JSONObject getJSON(int level) throws IOException {
        URL url;
        if (level < 3){
            url = new URL("https://opentdb.com/api.php?amount=1&category=9&difficulty=easy&type=multiple");
        }else if (level < 7){
            url = new URL("https://opentdb.com/api.php?amount=1&category=9&difficulty=medium&type=multiple");
        }else {
            url = new URL("https://opentdb.com/api.php?amount=1&category=9&difficulty=hard&type=multiple");
        }
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        System.out.println(content);
        String plainString = content.toString()
                .replaceAll("&lt;", "<");
//                .replaceAll("&gt;", ">")
//                .replaceAll("&amp;", "&")
//                .replaceAll("&nbsp;", " ")
//                .replaceAll("&quot;", "\\\"")
//                .replaceAll("&#039;", "'")
//                .replaceAll("&oacute;", "ó");
        System.out.println(plainString);


        return new JSONObject(plainString);
    }

    /**
     * Get questions as an ArrayList
     */
    private static ArrayList<String> getQuestion(int level) throws IOException {
        JSONObject object = getJSON(level);
        ArrayList<String> question = new ArrayList<>();;
        question.add(object.getJSONArray("results").getJSONObject(0).get("question").toString());
        question.add(object.getJSONArray("results").getJSONObject(0).get("correct_answer").toString());
        question.add(object.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").get(0).toString());
        question.add(object.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").get(1).toString());
        question.add(object.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").get(2).toString());
        return question;

    }

    public void promptUser(String question, String answer1, String answer2, String answer3, String answer4){
        JFrame frame = new JFrame();
        frame.setTitle("Question " + level);
        frame.setSize(450, 600);
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 10, 2, 2);
        JLabel label = new JLabel(question);
        JButton button1 = new JButton(answer1);

        JButton button2 = new JButton(answer2);
        JButton button3 = new JButton(answer3);
        JButton button4 = new JButton(answer4);
        button1.addActionListener(e -> {
            frame.dispose();
            answer = answer1;
            awaiting = false;
        });
        button2.addActionListener(e -> {
            frame.dispose();
            answer = answer2;
            awaiting = false;

        });
        button3.addActionListener(e -> {
            frame.dispose();
            answer = answer3;
            awaiting = false;

        });
        button4.addActionListener(e -> {
            frame.dispose();
            answer = answer4;
            awaiting = false;

        });
        c.insets = new Insets(1, 1, 2, 2);
        panel.add(label, c);
        c.gridy = 1;
        panel.add(button1, c);
        c.gridy = 2;
        panel.add(button2, c);
        c.gridy = 3;
        panel.add(button3, c);
        c.gridy = 4;
        panel.add(button4, c);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.pack();
        frame.setSize(frame.getWidth() + 20, frame.getHeight() + 20);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    }
    public void promptRes(GameState state, String correct){
        JFrame frame = new JFrame();
        frame.setTitle("Game result");
        frame.setSize(450, 600);
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label;
        JLabel label2;
        if (!correct.isEmpty()){
            label = new JLabel("You lost!");
            if (gameType == GameType.INFINITE){
                label.setText(label.getText() + "\nHowever, you are playing infinite!");
            }
            label2 = new JLabel("Correct answer: " + correct);
        }else {
            label = new JLabel("You won!");
            label2 = new JLabel("Press \"Done\" to start a new game");
        }

        JButton button = new JButton("Done");
        button.addActionListener(e -> {
            awaiting = false;
            frame.dispose();
        });
        panel.add(label);
        panel.add(label2);
        panel.add(button);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.pack();
        frame.setSize(frame.getWidth() + 100, frame.getHeight() + 20);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void promptHome(){
        JFrame frame = new JFrame();
        frame.setTitle("Home screen");
        frame.setSize(450, 600);
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Welcome to \"Who Wants to Be a Millionaire\"!");
        JLabel label2 = new JLabel("Please select a game");
        JButton button = new JButton("Classic");
        JButton button2 = new JButton("Infinite");
        JButton button3 = new JButton("Save Username");
        JLabel label3 = new JLabel("Current username: " + currentPlayer);
        if (currentPlayer != null){
            button3.setText("Change Username");
        }
        JButton button4 = new JButton("Exit");
        button.addActionListener(e -> {
            frame.dispose();
            gameType = GameType.CLASSIC;
            gameState = GameState.ONGOING;
            awaiting = false;
        });
        button2.addActionListener(e -> {
            frame.dispose();
            gameType = GameType.INFINITE;
            gameState = GameState.ONGOING;
            awaiting = false;
        });
        button3.addActionListener(e -> {
            frame.dispose();
            promptLogin();
        });
        button4.addActionListener(e -> {
            frame.dispose();
            gameState = GameState.ONGOING;
            awaiting = false;
            gameRunning = false;
        });
        panel.add(label);
        panel.add(label2);
        panel.add(button);
        panel.add(button2);
        panel.add(button3);
        if (currentPlayer != null){
            panel.add(label3);
            System.out.println("test");
        }
        panel.add(button4);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.pack();
        frame.setSize(frame.getWidth() + 100, frame.getHeight() + 20);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void promptLogin(){
        JFrame frame = new JFrame();
        frame.setTitle("Login");
        frame.setSize(450, 600);
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Please enter your username!");
        JTextArea username = new JTextArea();
        JButton button = new JButton("Login");
        button.addActionListener(e -> {
            frame.dispose();
            if (board.playerExists(username.getText())){
                board.addPlayer(new Player(username.getText(), 0, 0, 0));
                currentPlayer = board.getPlayer(username.getText());
            }
            else {
                currentPlayer = board.getPlayer(username.getText());
            }
            promptHome();
        });

        panel.add(label);
        panel.add(username);
        panel.add(button);
        frame.pack();
        frame.setSize(frame.getWidth() + 100, frame.getHeight() + 20);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        panel.setVisible(true);
    }
}
