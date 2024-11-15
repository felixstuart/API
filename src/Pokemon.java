import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Pokemon {
    public static String name;
    public static String[] abilities;
    public static String imageURL;
    public final String pokeURL;

    public Pokemon(JSONObject jsonObject) {
        pokeURL = null;
        name = jsonObject.get("name").toString();
        JSONArray abilityObjects = (JSONArray) jsonObject.get("abilities");
        abilities = (JSONArray) ((JSONArray) jsonObject.get("abilities")).stream().map(o -> {
            ((JSONObject) ((JSONObject) o).get("ability")).get("name"))});

    }

    public Pokemon(String pokeURL) {
        this.pokeURL = pokeURL;

    }
}
