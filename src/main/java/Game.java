import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private int level;
    private GameState gameState;
    boolean awaiting = true;

    public Game() throws IOException {
        this.level = 1;
        gameState = GameState.ONGOING;

        System.out.println("WW2BAM has been started!");


        init();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private void init() throws IOException {

        while (gameState == GameState.ONGOING){

            ArrayList<String> question = getQuestion(level);
            System.out.println(question.get(0));
            System.out.println("Answers:");
            System.out.println(question.get(1));
            System.out.println(question.get(2));
            System.out.println(question.get(3));
            System.out.println(question.get(4));

            awaiting = true;
            while (awaiting){
                promptUser(question.get(0), question.get(1), question.get(2), question.get(3), question.get(4));
            }


//            Scanner scan = new Scanner(System.in);
//            System.out.print("Response: ");
//            String res = scan.nextLine();
//            if (res.equals(question.get(1))){
//                level++;
//                System.out.println("Your level: " + level);
//                System.out.println("Correct! Next question");
//            }else{
//                gameState = GameState.LOST;
//                System.out.println("Incorrect!");
//            }
//
//            if (level > 10){
//                gameState = GameState.WON;
//            }

        }
        System.out.println("Your level: " + level);
        System.out.println(gameState.toString());

    }

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
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&quot;", "\\\"")
                .replaceAll("&#039;", "'")
                .replaceAll("&oacute;", "ó");
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
        frame.setSize(450, 75);
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(question);
        JButton button1 = new JButton(answer1);
        JButton button2 = new JButton(answer2);
        JButton button3 = new JButton(answer3);
        JButton button4 = new JButton(answer4);
        button1.addActionListener(e -> {
            frame.dispose();
            awaiting = false;
        });
        button2.addActionListener(e -> {
            frame.dispose();
            awaiting = false;
        });
        button3.addActionListener(e -> {
            frame.dispose();
            awaiting = false;
        });
        button4.addActionListener(e -> {
            frame.dispose();
            awaiting = false;
        });
        panel.add(label);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
