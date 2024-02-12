package speakthespiremod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.options.AbandonRunButton;
import com.megacrit.cardcrawl.screens.options.ExitGameButton;
import com.megacrit.cardcrawl.screens.options.OptionsPanel;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.buttons.CardSelectConfirmButton;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.ui.buttons.ReturnToMenuButton;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;

import basemod.ReflectionHacks;
import speakthespiremod.serializers.HitboxSerializer;
import speakthespiremod.serializers.MonsterSerializer;
import speakthespiremod.serializers.PlayerSerializer;
import speakthespiremod.serializers.PotionSerializer;
import speakthespiremod.serializers.RelicSerializer;
import speakthespiremod.serializers.RewardSerializer;
import speakthespiremod.serializers.ShopScreenSerializer;

public class SpeakTheSpireApi {
    public static void setupRoutes(SimpleServer server) {
        server.createGetEndpoint("/player", (Map<String, List<String>> requestParameters) -> {
            JsonValue playerJson = PlayerSerializer.toJson(AbstractDungeon.player);
            SpeakTheSpireMod.logger.debug("player json: " + playerJson.toJson(OutputType.json));
            return playerJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/monsters", (Map<String, List<String>> requestParameters) -> {
            JsonValue monstersJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                monstersJson.addChild(MonsterSerializer.toJson(monster));
            }
            SpeakTheSpireMod.logger.debug("monster json: " + monstersJson.toJson(OutputType.json));
            return monstersJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/potions", (Map<String, List<String>> requestParameters) -> {
            JsonValue potionsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractPotion potion : AbstractDungeon.player.potions) {
                potionsJson.addChild(PotionSerializer.toJson(potion));
            }
            SpeakTheSpireMod.logger.debug("potion json: " + potionsJson.toJson(OutputType.json));
            return potionsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/relics", (Map<String, List<String>> requestParameters) -> {
            JsonValue relicsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relicsJson.addChild(RelicSerializer.toJson(relic));
            }
            SpeakTheSpireMod.logger.debug("relic json: " + relicsJson.toJson(OutputType.json));
            return relicsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/rewards", (Map<String, List<String>> requestParameters) -> {
            JsonValue rewardsJson = new JsonValue(JsonValue.ValueType.array);
            for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
                rewardsJson.addChild(RewardSerializer.toJson(reward));
            }
            SpeakTheSpireMod.logger.debug("reward json: " + rewardsJson.toJson(OutputType.json));
            return rewardsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/bossRelics", (Map<String, List<String>> requestParameters) -> {
            JsonValue bossRelicsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractRelic bossRelic : AbstractDungeon.bossRelicScreen.relics) {
                bossRelicsJson.addChild(RelicSerializer.toJson(bossRelic));
            }
            SpeakTheSpireMod.logger.debug("bossRelic json: " + bossRelicsJson.toJson(OutputType.json));
            return bossRelicsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/screen", (Map<String, List<String>> requestParameters) -> {
            JsonValue screenJson = new JsonValue(JsonValue.ValueType.object);
            if (AbstractDungeon.screen != null)
                screenJson.addChild("AbstractDungeon.screen", new JsonValue(AbstractDungeon.screen.name()));
            if (CardCrawlGame.mainMenuScreen.screen != null)
                screenJson.addChild("CardCrawlGame.mainMenuScreen.screen",
                        new JsonValue(CardCrawlGame.mainMenuScreen.screen.name()));

            SpeakTheSpireMod.logger.debug("screen json: " + screenJson.toJson(OutputType.json));
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

            SpeakTheSpireMod.logger.debug("potionUi json: " + potionUiJson.toJson(OutputType.json));
            return potionUiJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/shop", (Map<String, List<String>> requestParameters) -> {
            JsonValue shopJson = ShopScreenSerializer.toJson(AbstractDungeon.shopScreen);
            SpeakTheSpireMod.logger.debug("shop json: " + shopJson.toJson(OutputType.json));
            return shopJson.toJson(OutputType.json);
        });

        server.createPostEndpoint("/navigate", (Map<String, List<String>> requestParameters) -> {
            String navItem = requestParameters.get("item").get(0);
            String numericValueString = requestParameters.get("numericValue").get(0);
            int numericValue = -1;
            if (numericValueString != null) {
                numericValue = Integer.parseInt(numericValueString);
            }

            SpeakTheSpireMod.logger.debug("Navigating to " + navItem);

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
                        CharacterOption selectedOption = null;
                        for (CharacterOption option : CardCrawlGame.mainMenuScreen.charSelectScreen.options) {
                            if (option.selected) {
                                selectedOption = option;
                                break;
                            }
                        }
                        if (selectedOption == null) {
                            return "No character selected";
                        }
                        if (numericValue >= 0) {
                            // ascension level specified so set it
                            CardCrawlGame.mainMenuScreen.charSelectScreen.isAscensionMode = true;
                            int maxAscensionLevel = ReflectionHacks.getPrivate(selectedOption, CharacterOption.class,
                                    "maxAscensionLevel");
                            if (numericValue >= 0) {
                                CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel = Math.min(numericValue,
                                        maxAscensionLevel);
                            }
                        } else {
                            // no number specified so toggle ascension mode
                            CardCrawlGame.mainMenuScreen.charSelectScreen.isAscensionMode = !CardCrawlGame.mainMenuScreen.charSelectScreen.isAscensionMode;
                        }
                        return "";
                    }

                    for (CharacterOption option : CardCrawlGame.mainMenuScreen.charSelectScreen.options) {
                        if (option.name.toLowerCase().contains(navItem)) {
                            option.hb.clicked = true;
                            return "";
                        }
                    }
                } else if (CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.RUN_HISTORY) {
                    if (navItem.equals("return") || navItem.equals("back")) {
                        CardCrawlGame.mainMenuScreen.runHistoryScreen.button.hb.clicked = true;
                        return "";
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
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.VICTORY
                        || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH) {
                    GameOverScreen gameOverScreen = AbstractDungeon.screen == AbstractDungeon.CurrentScreen.VICTORY
                            ? AbstractDungeon.victoryScreen
                            : AbstractDungeon.deathScreen;
                    if (gameOverScreenContinueCommands.contains(navItem)) {
                        ReturnToMenuButton returnButton = ReflectionHacks.getPrivate(gameOverScreen,
                                GameOverScreen.class,
                                "returnButton");
                        returnButton.hb.clicked = true;
                        return "";
                    }
                    return "Did not find navigation item";
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
                    if (navItem.equals("peek")) {
                        PeekButton peekButton = ReflectionHacks.getPrivate(AbstractDungeon.handCardSelectScreen,
                                HandCardSelectScreen.class,
                                "peekButton");
                        boolean isHidden = ReflectionHacks.getPrivate(peekButton, PeekButton.class,
                                "isHidden");
                        if (isHidden) {
                            return "Peek button is hidden";
                        }

                        peekButton.hb.clicked = true;
                        return "";
                    } else if (navItem.equals("confirm")) {
                        CardSelectConfirmButton button = AbstractDungeon.handCardSelectScreen.button;
                        boolean isHidden = ReflectionHacks.getPrivate(button, CardSelectConfirmButton.class,
                                "isHidden");
                        boolean isDisabled = button.isDisabled;
                        if (isHidden || isDisabled) {
                            return "Confirm button is hidden or disabled";
                        }

                        AbstractDungeon.handCardSelectScreen.button.hb.clicked = true;
                        return "";
                    }

                    return "Did not find navigation item";
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
                    if (navItem.equals("confirm")) {
                        GridSelectConfirmButton confirmButton = AbstractDungeon.gridSelectScreen.confirmButton;
                        boolean isHidden = ReflectionHacks.getPrivate(confirmButton, GridSelectConfirmButton.class,
                                "isHidden");
                        boolean isDisabled = confirmButton.isDisabled;
                        if (isHidden || isDisabled) {
                            return "Proceed button is hidden or disabled";
                        }

                        confirmButton.hb.clicked = true;
                        return "";
                    } else if (navItem.equals("cancel") || navItem.equals("return")) {
                        return clickOverlayCancelButton();
                    } else if (navItem.equals("peek")) {
                        PeekButton peekButton = AbstractDungeon.gridSelectScreen.peekButton;
                        boolean isHidden = ReflectionHacks.getPrivate(peekButton, PeekButton.class, "isHidden");
                        if (isHidden) {
                            return "Peek button is hidden";
                        }

                        peekButton.hb.clicked = true;
                        return "";
                    }

                    return "Did not find navigation item";
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD) {
                    if (navItem.equals("skip")) {
                        boolean skippable = ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen,
                                CardRewardScreen.class,
                                "skippable");
                        if (!skippable) {
                            return "Card reward screen is not skippable";
                        }

                        SkipCardButton skipButton = ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen,
                                CardRewardScreen.class,
                                "skipButton");
                        skipButton.hb.clicked = true;
                        return "";
                    } else if (navItem.equals("maxHP")) {
                        // TODO: implement singing bowl
                        return "Singing bowl not implemented";
                    } else if (navItem.equals("peek")) {
                        PeekButton peekButton = ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen,
                                CardRewardScreen.class,
                                "peekButton");
                        boolean isHidden = ReflectionHacks.getPrivate(peekButton, PeekButton.class, "isHidden");
                        if (isHidden) {
                            return "Peek button is hidden";
                        }

                        peekButton.hb.clicked = true;
                        return "";
                    }

                    return "Did not find navigation item";
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                    if (navItem.equals("skip") || navItem.equals("proceed")) {
                        return clickOverlayProceedButton();
                    }
                    return "Did not find navigation item";
                }

                if (navItem.equals("release")) {
                    AbstractDungeon.player.isInKeyboardMode = false;
                    return "";
                } else if (navItem.equals("proceed") || navItem.equals("skip")) {
                    return clickOverlayProceedButton();
                } else if (navItem.equals("cancel") || navItem.equals("return")) {
                    return clickOverlayCancelButton();
                }
            }

            if (navItem.equals("caw")) {
                int roll = MathUtils.random(2);
                if (roll == 0) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1A"));
                } else if (roll == 1) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1B"));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1C"));
                }
            }

            return "Did not find navigation item";
        });
    }

    private static String clickOverlayProceedButton() {
        ProceedButton proceedButton = AbstractDungeon.overlayMenu.proceedButton;
        boolean isHidden = ReflectionHacks.getPrivate(proceedButton, ProceedButton.class, "isHidden");
        if (isHidden) {
            return "Proceed button is hidden";
        }

        Hitbox hb = ReflectionHacks.getPrivate(proceedButton, ProceedButton.class, "hb");
        hb.clicked = true;
        return "";
    }

    private static String clickOverlayCancelButton() {
        CancelButton cancelButton = AbstractDungeon.overlayMenu.cancelButton;
        boolean isHidden = ReflectionHacks.getPrivate(cancelButton, CancelButton.class, "isHidden");
        if (isHidden) {
            return "Cancel button is hidden";
        }

        cancelButton.hb.clicked = true;
        return "";
    }

    private static void clickMainMenuButton(MenuButton.ClickResult result) {
        for (MenuButton button : CardCrawlGame.mainMenuScreen.buttons) {
            if (button.result == result) {
                button.buttonEffect();
                break;
            }
        }
    }

    // TODO: move this to a better place
    static final List<String> gameOverScreenContinueCommands = new ArrayList<>(
            Arrays.asList("continue", "proceed", "mainMenu"));
    static final Map<String, MenuButton.ClickResult> navItemToClickResult = new HashMap<>();
    static final Map<String, Integer> navItemToPanelIndex = new HashMap<>();
    static {
        navItemToClickResult.put("play", MenuButton.ClickResult.PLAY);
        navItemToClickResult.put("resume", MenuButton.ClickResult.RESUME_GAME);
        navItemToClickResult.put("continue", MenuButton.ClickResult.RESUME_GAME);
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
}
