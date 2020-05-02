package TheManiac.character;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.attack.AbyssShackle;
import TheManiac.cards.maniac_blue.attack.Strike_Maniac;
import TheManiac.cards.maniac_blue.skill.BurnBlood;
import TheManiac.cards.maniac_blue.skill.Defend_Maniac;
import TheManiac.relics.BrokenHorn;
import TheManiac.relics.DamagedAnvil;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;

import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.characters.CustomCharSelectInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static TheManiac.TheManiac.THE_MANIAC_SKELETON_ATLAS;
import static TheManiac.TheManiac.THE_MANIAC_SKELETON_JSON;
import static TheManiac.character.TheManiacCharacter.Enums.MANIAC_BLUE;
import static TheManiac.character.TheManiacCharacter.Enums.THE_MANIAC;

public class TheManiacCharacter extends AbstractPlayerWithMinions {
    public static final Logger logger = LogManager.getLogger(TheManiac.class.getName());
    private static final String ID = "TheManiac";
    public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final String NAME = charStrings.NAMES[0];
    public static final String DESCRIPTION = charStrings.TEXT[0];
    public static final String[] orbTextures = {
            "maniacMod/images/character/energyOrb/layer1.png",
            "maniacMod/images/character/energyOrb/layer2.png",
            "maniacMod/images/character/energyOrb/layer3.png",
            "maniacMod/images/character/energyOrb/layer4.png",
            "maniacMod/images/character/energyOrb/layer5.png",
            "maniacMod/images/character/energyOrb/layer6.png",
            "maniacMod/images/character/energyOrb/layer1d.png",
            "maniacMod/images/character/energyOrb/layer2d.png",
            "maniacMod/images/character/energyOrb/layer3d.png",
            "maniacMod/images/character/energyOrb/layer4d.png",
            "maniacMod/images/character/energyOrb/layer5d.png",
    };
    public static final String orbVFX = "maniacMod/images/character/energyOrb/vfx.png";
    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 82;
    public static final int MAX_HP = 82;
    public static final int STARTING_GOLD = 99;
    public static final int ORB_SLOTS = 3;
    public static final int DRAW_PER_TURN = 5;
    public static final int MAX_MINIONS = 3;
    public static final String ANIMATION = "maniacMod/images/character/ManiacSpriter/theManiacCharacter.scml";
    
    public int weaponUpgrades;
    public int minionsUpgrades;

    @Override
    public CustomCharSelectInfo getInfo() {
        return new CustomCharSelectInfo(NAME, DESCRIPTION, STARTING_HP, MAX_HP, ORB_SLOTS, MAX_MINIONS,STARTING_GOLD, DRAW_PER_TURN,
                this, this.getStartingRelics(), this.getStartingDeck(), false);
    }

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_MANIAC;
        @SpireEnum(name = "MANIAC_BLUE_COLOR")
        public static AbstractCard.CardColor MANIAC_BLUE;
        @SpireEnum(name = "MANIAC_BLUE_COLOR")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    public static class CustomAttackEffect {
        @SpireEnum public static AbstractGameAction.AttackEffect EXOTIC_POISON;
        @SpireEnum public static AbstractGameAction.AttackEffect MANIC_BLUNT;
        @SpireEnum public static AbstractGameAction.AttackEffect MANIC_CLEAVE;
        @SpireEnum public static AbstractGameAction.AttackEffect MANIC_SLASH;
    }

    public TheManiacCharacter(String name) {
        super(name, THE_MANIAC, orbTextures, orbVFX, (String)null, null);
        this.initializeClass(null, TheManiac.THE_MANIAC_SHOULDER_2, TheManiac.THE_MANIAC_SHOULDER_1,
                TheManiac.THE_MANIAC_CORPSE, this.getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));
        loadAnimation(THE_MANIAC_SKELETON_ATLAS, THE_MANIAC_SKELETON_JSON, 1.0f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1f);
        e.setTimeScale(0.8F);
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
        this.weaponUpgrades = 0;
        this.minionsUpgrades = 0;
    }
    
    public void changeState(String state) {
        switch (state) {
            case "Attack":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0f);
                break;
            case "Hit":
                this.state.setAnimation(0, "Hit", false);
                this.state.addAnimation(0, "Idle", true, 0f);
                break;
            case "Call":
                this.state.setAnimation(0, "Call", false);
                this.state.addAnimation(0, "Idle", true, 0f);
        }
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList();
        logger.info("Now loading The Maniac's starting deck");
        retVal.add(Strike_Maniac.ID);
        retVal.add(Strike_Maniac.ID);
        retVal.add(Strike_Maniac.ID);
        retVal.add(Strike_Maniac.ID);
        retVal.add(Strike_Maniac.ID);
        retVal.add(Defend_Maniac.ID);
        retVal.add(Defend_Maniac.ID);
        retVal.add(Defend_Maniac.ID);
        retVal.add(Defend_Maniac.ID);
        retVal.add(BurnBlood.ID);
        retVal.add(AbyssShackle.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(DamagedAnvil.ID);
        retVal.add(BrokenHorn.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION, STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, DRAW_PER_TURN,
                this, this.getStartingRelics(), this.getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return MANIAC_BLUE;
    }

    @Override
    public Color getCardRenderColor() {
        return TheManiac.THE_MANIAC_BLUE;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new BurnBlood();
    }

    @Override
    public Color getCardTrailColor() {
        return TheManiac.THE_MANIAC_BLUE;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 6;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.2f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.MED, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheManiacCharacter(name);
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return TheManiac.THE_MANIAC_BLUE;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.LIGHTNING, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.LIGHTNING
        };
    }

    @Override
    public String getVampireText() {
        return charStrings.TEXT[2];
    }
}
