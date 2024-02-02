package saythespiremod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.options.AbandonRunButton;
import com.megacrit.cardcrawl.screens.options.ExitGameButton;
import com.megacrit.cardcrawl.screens.options.OptionsPanel;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;

import basemod.ReflectionHacks;
import saythespiremod.serializers.HitboxSerializer;
import saythespiremod.serializers.MonsterSerializer;
import saythespiremod.serializers.PlayerSerializer;
import saythespiremod.serializers.PotionSerializer;
import saythespiremod.serializers.RelicSerializer;
import saythespiremod.serializers.RewardSerializer;
import saythespiremod.serializers.ShopScreenSerializer;

public class SayTheSpireApi {
    public static void setupRoutes(SimpleServer server) {
        server.createGetEndpoint("/player", (Map<String, List<String>> requestParameters) -> {
            JsonValue playerJson = PlayerSerializer.toJson(AbstractDungeon.player);
            SayTheSpireMod.logger.debug("player json: " + playerJson.toJson(OutputType.json));
            return playerJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/monsters", (Map<String, List<String>> requestParameters) -> {
            JsonValue monstersJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                monstersJson.addChild(MonsterSerializer.toJson(monster));
            }
            SayTheSpireMod.logger.debug("monster json: " + monstersJson.toJson(OutputType.json));
            return monstersJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/potions", (Map<String, List<String>> requestParameters) -> {
            JsonValue potionsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractPotion potion : AbstractDungeon.player.potions) {
                potionsJson.addChild(PotionSerializer.toJson(potion));
            }
            SayTheSpireMod.logger.debug("potion json: " + potionsJson.toJson(OutputType.json));
            return potionsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/relics", (Map<String, List<String>> requestParameters) -> {
            JsonValue relicsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relicsJson.addChild(RelicSerializer.toJson(relic));
            }
            SayTheSpireMod.logger.debug("relic json: " + relicsJson.toJson(OutputType.json));
            return relicsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/rewards", (Map<String, List<String>> requestParameters) -> {
            JsonValue rewardsJson = new JsonValue(JsonValue.ValueType.array);
            for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
                rewardsJson.addChild(RewardSerializer.toJson(reward));
            }
            SayTheSpireMod.logger.debug("reward json: " + rewardsJson.toJson(OutputType.json));
            return rewardsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/bossRelics", (Map<String, List<String>> requestParameters) -> {
            JsonValue bossRelicsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractRelic bossRelic : AbstractDungeon.bossRelicScreen.relics) {
                bossRelicsJson.addChild(RelicSerializer.toJson(bossRelic));
            }
            SayTheSpireMod.logger.debug("bossRelic json: " + bossRelicsJson.toJson(OutputType.json));
            return bossRelicsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/screen", (Map<String, List<String>> requestParameters) -> {
            JsonValue screenJson = new JsonValue(JsonValue.ValueType.object);
            if (AbstractDungeon.screen != null)
                screenJson.addChild("AbstractDungeon.screen", new JsonValue(AbstractDungeon.screen.name()));
            if (CardCrawlGame.mainMenuScreen.screen != null)
                screenJson.addChild("CardCrawlGame.mainMenuScreen.screen",
                        new JsonValue(CardCrawlGame.mainMenuScreen.screen.name()));

            SayTheSpireMod.logger.debug("screen json: " + screenJson.toJson(OutputType.json));
            return screenJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/potionUi", (Map<String, List<String>> requestParameters) -> {
            Hitbox topHitbox = ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi, PotionPopUp.class,
                    "hbTop");
            Hitbox bottomHitbox = ReflectionHacks.getPrivate(AbstractDungeon.topPanel.potionUi, PotionPopUp.class,
                    "hbBot");

            JsonValue potionUiJson = new JsonValue(JsonValue.ValueType.object);
            potionUiJson.addChild("isHidden", new JsonValue(AbstractDungeon.topPanel.potionUi.isHidden));
            potionUiJson.addChild("topButton", HitboxSerializer.toJson(topHitbox));
            potionUiJson.addChild("bottomButton", HitboxSerializer.toJson(bottomHitbox));

            SayTheSpireMod.logger.debug("potionUi json: " + potionUiJson.toJson(OutputType.json));
            return potionUiJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/shop", (Map<String, List<String>> requestParameters) -> {
            JsonValue shopJson = ShopScreenSerializer.toJson(AbstractDungeon.shopScreen);
            SayTheSpireMod.logger.debug("shop json: " + shopJson.toJson(OutputType.json));
            return shopJson.toJson(OutputType.json);
        });

        server.createPostEndpoint("/navigate", (Map<String, List<String>> requestParameters) -> {
            String navItem = requestParameters.get("item").get(0);

            SayTheSpireMod.logger.debug("Navigating to " + navItem);

            if (CardCrawlGame.mode == CardCrawlGame.GameMode.CHAR_SELECT) {
                if (CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.MAIN_MENU) {
                    if (navItemToClickResult.containsKey(navItem)) {
                        clickMainMenuButton(navItemToClickResult.get(navItem));
                        return "";
                    }
                } else if (CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.PANEL_MENU) {
                    if (navItem.equals("back")) {
                        CardCrawlGame.mainMenuScreen.panelScreen.button.hb.clicked = true;
                        return "";
                    }

                    if (navItemToPanelIndex.containsKey(navItem)) {
                        CardCrawlGame.mainMenuScreen.panelScreen.panels
                                .get(navItemToPanelIndex.get(navItem)).hb.clicked = true;
                        return "";
                    }
                } else if (CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.CHAR_SELECT) {
                    if (navItem.equals("back")) {
                        CardCrawlGame.mainMenuScreen.charSelectScreen.cancelButton.hb.clicked = true;
                        return "";
                    } else if (navItem.equals("embark")) {
                        CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.hb.clicked = true;
                        return "";
                    } else if (navItem.equals("ascension")) {
                        CardCrawlGame.mainMenuScreen.charSelectScreen.isAscensionMode = !CardCrawlGame.mainMenuScreen.charSelectScreen.isAscensionMode;
                        return "";
                    }
                    // TODO: set ascension level
                    // TODO: set seed

                    for (CharacterOption option : CardCrawlGame.mainMenuScreen.charSelectScreen.options) {
                        if (option.name.toLowerCase().contains(navItem)) {
                            option.hb.clicked = true;
                            return "";
                        }
                    }
                }
            } else if (CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY) {
                if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SETTINGS) {
                    if (navItem.equals("saveAndQuit")) {
                        ExitGameButton exitButton = ReflectionHacks.getPrivate(AbstractDungeon.settingsScreen.panel,
                                OptionsPanel.class,
                                "exitBtn");
                        Hitbox hb = ReflectionHacks.getPrivate(exitButton, ExitGameButton.class, "hb");
                        hb.clicked = true;
                        return "";
                    } else if (navItem.equals("abandonRun")) {
                        AbandonRunButton abandonButton = ReflectionHacks.getPrivate(
                                AbstractDungeon.settingsScreen.panel,
                                OptionsPanel.class,
                                "abandonBtn");
                        Hitbox hb = ReflectionHacks.getPrivate(abandonButton, AbandonRunButton.class, "hb");
                        hb.clicked = true;
                        return "";
                    } else if (navItem.equals("yes")) {
                        if (AbstractDungeon.settingsScreen.exitPopup.shown) {
                            AbstractDungeon.settingsScreen.exitPopup.yesHb.clicked = true;
                            return "";
                        } else if (AbstractDungeon.settingsScreen.abandonPopup.shown) {
                            AbstractDungeon.settingsScreen.abandonPopup.yesHb.clicked = true;
                            return "";
                        }
                        return "No known popup showing with yes button";
                    } else if (navItem.equals("no")) {
                        if (AbstractDungeon.settingsScreen.exitPopup.shown) {
                            AbstractDungeon.settingsScreen.exitPopup.noHb.clicked = true;
                            return "";
                        } else if (AbstractDungeon.settingsScreen.abandonPopup.shown) {
                            AbstractDungeon.settingsScreen.abandonPopup.noHb.clicked = true;
                            return "";
                        }
                        return "No known popup showing with no button";
                    }
                    return "Did not find navigation item";
                }
            }

            if (navItem.equals("reset")) {
                AbstractDungeon.player.isInKeyboardMode = false;
                return "";
            }

            return "Did not find navigation item";
        });
    }

    // TODO: move this to a better place
    static final Map<String, MenuButton.ClickResult> navItemToClickResult = new HashMap<>();
    static final Map<String, Integer> navItemToPanelIndex = new HashMap<>();
    static {
        navItemToClickResult.put("play", MenuButton.ClickResult.PLAY);
        navItemToClickResult.put("resume", MenuButton.ClickResult.RESUME_GAME);
        navItemToClickResult.put("abandon", MenuButton.ClickResult.ABANDON_RUN);
        navItemToClickResult.put("compendium", MenuButton.ClickResult.INFO);
        navItemToClickResult.put("statistics", MenuButton.ClickResult.STAT);
        navItemToClickResult.put("settings", MenuButton.ClickResult.SETTINGS);
        navItemToClickResult.put("patchNotes", MenuButton.ClickResult.PATCH_NOTES);
        navItemToClickResult.put("quit", MenuButton.ClickResult.QUIT);

        navItemToPanelIndex.put("standard", 0);
        navItemToPanelIndex.put("dailyClimb", 1);
        navItemToPanelIndex.put("custom", 2);
        navItemToPanelIndex.put("cardLibrary", 0);
        navItemToPanelIndex.put("relicCollection", 1);
        navItemToPanelIndex.put("potionLab", 2);
        navItemToPanelIndex.put("characterStats", 0);
        navItemToPanelIndex.put("statistics", 0); // double usage of "statistics"
        navItemToPanelIndex.put("leaderboard", 1);
        navItemToPanelIndex.put("runHistory", 2);
        navItemToPanelIndex.put("gameSettings", 0);
        navItemToPanelIndex.put("inputSettings", 1);
        navItemToPanelIndex.put("credits", 2);
    }

    public static void clickMainMenuButton(MenuButton.ClickResult result) {
        for (MenuButton button : CardCrawlGame.mainMenuScreen.buttons) {
            if (button.result == result) {
                button.buttonEffect();
                break;
            }
        }
    }
}
