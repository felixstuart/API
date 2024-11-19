import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;


// video to load jar
//https://www.youtube.com/watch?v=QAJ09o3Xl_0

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Program for print data in JSON format.
public class ReadJson {
    final private int HEIGHT = 800;
    final private int WIDTH = 700;

    public static void main(String args[]) throws ParseException, java.text.ParseException {
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

    public ReadJson() throws java.text.ParseException, ParseException {
        Pokemon pokemon = null;
        try {
            pokemon = pull("ditto");
        } catch (Exception e) {

        }
        makeUI(pokemon);
    }

    private void makeUI(Pokemon pokemon) {
        JFrame pokedex = new JFrame();
        pokedex.setSize(WIDTH, HEIGHT);
        pokedex.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel sprite = new JLabel(pokemon.sprite);
        sprite.setSize(1000, 1000);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        pokedex.add(sprite, c);

        JPanel name = new JPanel();

        JLabel nameTitleText = new JLabel("Name");
        nameTitleText.setFont(new Font("Tahoma", Font.BOLD, 14));
        JLabel contentText = new JLabel(capFirstLetter(pokemon.name));

        name.setLayout(new GridLayout(2, 1));
        name.add(nameTitleText);
        name.add(contentText);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        pokedex.add(name, c);

        JPanel abilities = new JPanel();
        abilities.setLayout(new GridLayout(3, 1));
        JLabel abilitiesTitleText = new JLabel("Abilities");
        abilitiesTitleText.setFont(new Font("Tahoma", Font.BOLD, 14));
        abilities.add(abilitiesTitleText);

        for (String abilityText : pokemon.abilities) {
            JLabel abilityTextJLabel = new JLabel(capFirstLetter(abilityText));
            abilities.add(abilityTextJLabel);
        }
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.5;
        pokedex.add(abilities, c);




        pokedex.setVisible(true);

    }

    private JPanel newTitleAndContentVertical(String title, String content) {
        JPanel panel = new JPanel();

        JLabel titleText = new JLabel(title);
        titleText.setFont(new Font("Tahoma", Font.BOLD, 14));
        JLabel contentText = new JLabel(content);

        panel.setLayout(new GridLayout(2, 1));
        panel.add(titleText);
        panel.add(contentText);

        return panel;
    }

    public Pokemon pull(String name) throws ParseException {
        String output = "abc";
        String totaljson = "";
        try {

            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + name);
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
            return new Pokemon(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String capFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}


