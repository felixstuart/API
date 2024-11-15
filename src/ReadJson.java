import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;


// video to load jar
//https://www.youtube.com/watch?v=QAJ09o3Xl_0

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Program for print data in JSON format.
public class ReadJson {
    final private int HEIGHT = 800;
    final private int WIDTH = 700;

    public static void main(String args[]) throws ParseException {
        // In java JSONObject is used to create JSON object
        // which is a subclass of java.util.HashMap.

        JSONObject file = new JSONObject();
        file.put("Full Name", "Ritu Sharma");
        file.put("Roll No.", 1704310046);
        file.put("Tution Fees", 65400);

        // To print in JSON format.
        System.out.print(file.get("Tution Fees"));
        ReadJson readingIsWhat = new ReadJson();

    }

    public ReadJson() {
        makeUI();
        try {
            pull();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void makeUI(Pokemon pokemon) {
        JFrame pokedex = new JFrame();
        pokedex.setSize(WIDTH, HEIGHT);
        pokedex.setLayout(new GridLayout(2,1));

        JLabel nameTitle = new JLabel("Name");
        JLabel nameText = new JLabel("Ditto");

        pokedex.add(newTitleAndContentVertical("Name", "Ditto"));

        JLabel ablilitiesTitle = new JLabel("Abilities");
        ablilitiesTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
        pokedex.add(ablilitiesTitle);
        pokedex.setVisible(true);

    }

    private JPanel newTitleAndContentVertical(String title, String content) {
        JPanel panel = new JPanel();

        JLabel titleText = new JLabel(title);
        titleText.setFont(new Font("Tahoma", Font.BOLD, 14));
        JLabel contentText = new JLabel(content);

        panel.setLayout(new GridLayout(2,1));
        panel.add(titleText);
        panel.add(contentText);

        return panel;
    }

    public void pull() throws ParseException {
        String output = "abc";
        String totaljson = "";
        try {

            URL url = new URL("https://pokeapi.co/api/v2/pokemon/ditto");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {

                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                totaljson += output;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        //System.out.println(str);
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(totaljson);
        System.out.println(jsonObject);

        try {


            System.out.println(jsonObject);
            System.out.println(jsonObject.get("name").toString());
            JSONArray abilities = (JSONArray) jsonObject.get("abilities");
            System.out.print("Abilities:");
            for (Object ability : abilities) {
                System.out.print(", " + ((JSONObject) ((JSONObject) ability).get("ability")).get("name"));
            }
            System.out.println(jsonObject.get("order"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}


