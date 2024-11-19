import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.*;


public class Pokemon {
    public String name;
    public List<String> abilities;
    public String imageURL;
    public String pokeURL;
    public ImageIcon sprite;

    public Pokemon(JSONObject jsonObject) {
        pokeURL = null;
        name = jsonObject.get("name").toString();
        JSONArray abilityObjects = (JSONArray) jsonObject.get("abilities");

        abilities = abilityObjects.stream().map(abilityObject -> {
            JSONObject ability = (JSONObject) abilityObject;
            return (String) ((JSONObject) ((JSONObject) abilityObject).get("ability")).get("name");
        }).toList();

        imageURL = (String) ((JSONObject) jsonObject.get("sprites")).get("front_default");
//        partially adapted from https://chatgpt.com/c/673bd6dd-8eb4-8001-a9c3-7f704b44d03c
        sprite = null;
        try {
            URL url = URI.create("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png").toURL();
            BufferedImage bufferedImage = ImageIO.read(url);
            sprite = new ImageIcon(bufferedImage.getScaledInstance(300,300, BufferedImage.SCALE_SMOOTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Pokemon(URL pokeURL) throws ParseException, org.json.simple.parser.ParseException {
        this.pokeURL = String.valueOf(pokeURL);
        JSONObject jsonObject = pull();

        name = jsonObject.get("name").toString();
        JSONArray abilityObjects = (JSONArray) jsonObject.get("abilities");

        abilities = abilityObjects.stream().map(abilityObject -> {
            JSONObject ability = (JSONObject) abilityObject;
            return ((JSONObject) ((JSONObject) abilityObject).get("ability")).get("name");
        }).toList();

        imageURL = (String) ((JSONObject) jsonObject.get("sprites")).get("front_default");
    }

    public JSONObject pull() throws ParseException, org.json.simple.parser.ParseException {
        String output = "abc";
        String totaljson = "";
        try {

            URL url = new URL(pokeURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {

                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                totaljson += output;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(totaljson);

        try {
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String makePokeURL(String pokemonName) {
        return ("https://pokeapi.co/api/v2/pokemon/" + pokemonName);
    }
}
