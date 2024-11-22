import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;


// video to load jar
//https://www.youtube.com/watch?v=QAJ09o3Xl_0

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Program for print data in JSON format.
public class ReadJson {
    final private int HEIGHT = 800;
    final private int WIDTH = 700;

    private Pokemon activePokemon;

    public static void main(String args[]) throws Exception {
        // In java JSONObject is used to create JSON object
        // which is a subclass of java.util.HashMap.

        JSONObject file = new JSONObject();
        file.put("Full Name", "Ritu Sharma");
        file.put("Roll No.", 1704310046);
        file.put("Tution Fees", 65400);

        // To print in JSON format.
        ReadJson readingIsWhat = new ReadJson();

    }

    public ReadJson() throws Exception {
        Pokemon pokemon = null;
        try {
            pokemon = pull("ditto");
        } catch (Exception e) {
            e.printStackTrace();
        }
        activePokemon = pokemon;
        makeUI();
    }

    private void makeUI() {
        JFrame pokedex = new JFrame();
        pokedex.setSize(WIDTH, HEIGHT);
        pokedex.setLayout(new GridBagLayout());

        addPokemon(activePokemon, pokedex);

        GridBagConstraints c = new GridBagConstraints();

        JButton backward = new JButton("<-");

        backward.addActionListener(e -> {
            try {
                activePokemon = fetchNextOrLastPokemon(activePokemon.index, PokedexDirection.BACKWARD);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            addPokemon(activePokemon, pokedex);
//            pokedex.revalidate();
        });

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        pokedex.add(backward,c);

        JButton forward = new JButton("->");
        forward.addActionListener(e -> {
            try {
                activePokemon = fetchNextOrLastPokemon(activePokemon.index, PokedexDirection.FORWARD);
                System.out.println(activePokemon.name);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            pokedex.removeAll();
            pokedex.repaint();
            makeUI();
//            pokedex.revalidate();
        });
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0;
        pokedex.add(forward,c);

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
        try {
            return new Pokemon(jsonObject);
        } catch (Exception e){
            throw e;
        }
//        System.out.println(jsonObject);
    }

    private String capFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private Pokemon fetchNextOrLastPokemon(int currentId, PokedexDirection direction ) throws ParseException {
        String output = "abc";
        String totaljson = "";
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + (currentId + (direction.equals(PokedexDirection.FORWARD) ? 1 : -1)));
            System.out.println(url);
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
            return new Pokemon(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addPokemon(Pokemon pokemon, JFrame mainframe) {
        assert mainframe.getLayout().equals(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon spriteImage = null;
        //        partially adapted from https://chatgpt.com/c/673bd6dd-8eb4-8001-a9c3-7f704b44d03c
        try {
            URL url = URI.create("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png").toURL();
            BufferedImage bufferedImage = ImageIO.read(url);
            spriteImage = new ImageIcon(bufferedImage.getScaledInstance(300,300, BufferedImage.SCALE_SMOOTH));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel sprite = new JLabel(spriteImage);
        sprite.setSize(1000, 1000);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        mainframe.add(sprite, c);

        JPanel name = new JPanel();

        JLabel nameTitleText = new JLabel("Name");
        nameTitleText.setFont(new Font("Tahoma", Font.BOLD, 14));
        JLabel contentText = new JLabel(capFirstLetter(activePokemon.name));

        name.setLayout(new GridLayout(2, 1));
        name.add(nameTitleText);
        name.add(contentText);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        mainframe.add(name, c);

        JPanel abilities = new JPanel();
        abilities.setLayout(new GridLayout(3, 1));
        JLabel abilitiesTitleText = new JLabel("Abilities");
        abilitiesTitleText.setFont(new Font("Tahoma", Font.BOLD, 14));
        abilities.add(abilitiesTitleText);

        for (String abilityText : activePokemon.abilities) {
            JLabel abilityTextJLabel = new JLabel(capFirstLetter(abilityText));
            abilities.add(abilityTextJLabel);
        }
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.5;
        mainframe.add(abilities, c);
        JButton backward = new JButton("<-");

        backward.addActionListener(e -> {
            try {
                activePokemon = fetchNextOrLastPokemon(activePokemon.index, PokedexDirection.BACKWARD);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            mainframe.removeAll();
            mainframe.repaint();
            addPokemon(activePokemon, mainframe);
            mainframe.revalidate();
//            pokedex.revalidate();
        });

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        mainframe.add(backward,c);

        JButton forward = new JButton("->");
        forward.addActionListener(e -> {
            try {
                activePokemon = fetchNextOrLastPokemon(activePokemon.index, PokedexDirection.FORWARD);
                System.out.println(activePokemon.name);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
//            mainframe.removeAll();
//            mainframe.repaint();
////            makeUI();
////            pokedex.revalidate();
        });
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0;
        mainframe.add(forward,c);
    }
}

enum PokedexDirection {
   FORWARD,
    BACKWARD
}

