package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ekylibre.exceptions.HTTPException;
import ekylibre.util.antlr4.Grammar;
import ekylibre.zero.BuildConfig;

/**
 * Created by antoine on 22/04/16.
 */

public class Equipment {

    private static final String TAG = "Equipment";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRENCH);

    public int id;
    public String name;
    public String number;
    public String variety;
    public String abilities;
    public Date deadAt;

    public static List<Equipment> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/equipments || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/equipments", attributes);
        List<Equipment> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Equipment(json.getJSONObject(i)));

        return array;
    }

    private Equipment(JSONObject object) throws JSONException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Equipment : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        number = object.getString("number");
        variety = object.getString("variety");

        if (!object.isNull("dead_at"))
            deadAt = sdf.parse(object.getString("dead_at"));

        abilities = null;
        if (!object.isNull("abilities")) {
            // Fill abilities
            JSONArray array = object.getJSONArray("abilities");
            StringBuilder sb = new StringBuilder();
            for (int i=0; i < array.length(); i++)
                sb.append("can ").append(array.getString(i)).append(",");

            // Fill varieties
            List<String> varieties = Grammar.computeItemAbilities("is " + variety);
            for (String var : varieties) {
                sb.append(var);
                if (varieties.indexOf(var) < varieties.size() - 1)
                    sb.append(",");
            }

            abilities = sb.toString();
        }

//        Grammar grammar = new Grammar();
//        abilities = null;
//        if (!object.isNull("abilities")) {
//            JSONArray array = object.getJSONArray("abilities");
//            StringBuilder sb = new StringBuilder();
//
//            for (int i=0; i < array.length(); i++) {
//                List<String> parents = grammar.computeAbilities("can " + array.getString(i), true);
//                for (String parent : parents) {
//                    if (!sb.toString().contains(parent))
//                        sb.append(parent).append(",");
//                }
//            }
//
//            // Adds parents variety
//            List<String> parents = grammar.computeAbilities("is " + variety, true);
//            for (String parent : parents) {
//                sb.append(parent);
//                if (parents.indexOf(parent) < parents.size() - 1)
//                    sb.append(",");
//            }
//
//            abilities = sb.toString();
//        }
    }
}
