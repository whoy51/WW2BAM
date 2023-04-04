import org.json.JSONObject;

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

            Scanner scan = new Scanner(System.in);
            System.out.print("Response: ");
            String res = scan.nextLine();
            if (res.equals(question.get(1))){
                level++;
                System.out.println("Your level: " + level);
                System.out.println("Correct! Next question");
            }else{
                gameState = GameState.LOST;
                System.out.println("Incorrect!");
            }

            if (level > 10){
                gameState = GameState.WON;
            }

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
                .replaceAll("&quot;", "\"")
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
}
