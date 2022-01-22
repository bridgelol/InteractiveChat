package com.loohp.interactivechat.objectholders;

import com.loohp.interactivechat.InteractiveChat;
import com.loohp.interactivechat.objectholders.CustomPlaceholder.ClickEventAction;
import com.loohp.interactivechat.objectholders.CustomPlaceholder.CustomPlaceholderClickEvent;
import com.loohp.interactivechat.objectholders.CustomPlaceholder.CustomPlaceholderHoverEvent;
import com.loohp.interactivechat.objectholders.CustomPlaceholder.CustomPlaceholderReplaceText;
import com.loohp.interactivechat.objectholders.CustomPlaceholder.ParsePlayer;
import com.loohp.interactivechat.utils.HTTPRequestUtils;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WebData {

    public static final String URL = "https://api.loohpjames.com/spigot/plugins/interactivechat";
    private static WebData INSTANCE = null;
    private JSONObject json;
    private List<CustomPlaceholder> specialPlaceholders;

    @SuppressWarnings("unchecked")
    private WebData() {
        specialPlaceholders = new ArrayList<>();
        json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("special-placeholders", array);

        JSONObject nana = new JSONObject();
        nana.put("parseplayer", "VIEWER");
        nana.put("keyword", "(?i)\\[NARS~\\]");
        nana.put("cooldown", 0);
        nana.put("hoverEnabled", true);
        nana.put("hoverText", "\u00a77\u00a76\u00a7oStay Wild.\u00a7f\n     \u00a77~\u00a7e\u00a7lN \u00a7b\u00a7o(IC Author's \u00a7c\u00a7oAdorable\u00a7b\u00a7o)");
        nana.put("clickEnabled", true);
        nana.put("clickAction", "OPEN_URL");
        nana.put("clickValue", "https://www.instagram.com/narliar/");
        nana.put("replaceEnabled", true);
        nana.put("replaceText", "\u00a7e[\u00a76narliar\u00a7e]");
        nana.put("name", "[NARS~]");
        array.add(nana);

        reload();
        run();
    }

    public static WebData getInstance() {
        return INSTANCE;
    }

    public static WebData newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebData();
        }
        return INSTANCE;
    }

    private void run() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(InteractiveChat.plugin, () -> {
            reload();
        }, 18000, 18000);
    }

    public void reload() {
        JSONObject newJson = HTTPRequestUtils.getJSONResponse(URL);
        if (newJson != null) {
            json = newJson;
        }

        List<CustomPlaceholder> specialPlaceholders = new ArrayList<>();
        for (Object obj : (JSONArray) json.get("special-placeholders")) {
            JSONObject each = (JSONObject) obj;
            specialPlaceholders.add(new CustomPlaceholder(-1, ParsePlayer.valueOf((String) each.get("parseplayer")), Pattern.compile((String) each.get("keyword")), true, Long.parseLong(each.get("cooldown").toString()), new CustomPlaceholderHoverEvent((boolean) each.get("hoverEnabled"), (String) each.get("hoverText")), new CustomPlaceholderClickEvent((boolean) each.get("clickEnabled"), ClickEventAction.valueOf((String) each.get("clickAction")), (String) each.get("clickValue")), new CustomPlaceholderReplaceText((boolean) each.get("replaceEnabled"), (String) each.get("replaceText")), (String) each.get("name"), ""));
        }

        this.specialPlaceholders = specialPlaceholders;
    }

    public List<CustomPlaceholder> getSpecialPlaceholders() {
        return specialPlaceholders;
    }

}
