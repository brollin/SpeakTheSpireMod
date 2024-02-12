package speakthespiremod.serializers;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;

import basemod.ReflectionHacks;

public class ShopScreenSerializer {
    public static JsonValue toJson(ShopScreen shopScreen) {
        JsonValue shopJson = new JsonValue(ValueType.object);
        shopJson.addChild("coloredCards", CardSerializer.toJson(shopScreen.coloredCards));
        shopJson.addChild("colorlessCards", CardSerializer.toJson(shopScreen.colorlessCards));

        ArrayList<StoreRelic> relics = ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "relics");
        shopJson.addChild("relics", StoreRelicSerializer.toJson(relics));

        ArrayList<StorePotion> potions = ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "potions");
        shopJson.addChild("potions", StorePotionSerializer.toJson(potions));

        JsonValue removalServiceJson = new JsonValue(ValueType.object);
        float purgeCardX = ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "purgeCardX");
        removalServiceJson.addChild("x", new JsonValue(purgeCardX));
        float purgeCardY = ReflectionHacks.getPrivate(shopScreen, ShopScreen.class, "purgeCardY");
        removalServiceJson.addChild("y", new JsonValue(purgeCardY));
        shopJson.addChild("removalService", removalServiceJson);

        return shopJson;
    }

}
