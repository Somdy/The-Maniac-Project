package TheManiac.cards.the_possessed;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.helper.ManiacUtils;
import TheManiac.helper.ThePossessedCardSavior;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ManiacRisksCard extends AbstractManiacCard {
    private static final Logger logger = LogManager.getLogger(ManiacRisksCard.class.getName());
    public static final String[] identifiers = {"ri", "my", "un", "es", "tr", "of", "tch", "or", "re", "cal", "in", "even", "odd"};
    public String riskID;
    public static int IDCounter = 0;
    public int combatCounter;
    public int counter;
    
    public ManiacRisksCard(String id, String img, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, CardCrawlGame.languagePack.getCardStrings(id).NAME, img, cost, CardCrawlGame.languagePack.getCardStrings(id).DESCRIPTION, 
                type, TheManiacCharacter.Enums.MANIAC_POSSESSED, rarity, target);
        combatCounter = 0;
        counter = 0;
        
        setOutlook();
    }
    
    public void setOutlook() {
        
    }
    
    public static class Enum {
        @SpireEnum(name = "THE_SHINIES")
        public static AbstractCard.CardRarity THE_SHINIES;
        @SpireEnum(name = "THE_UNCERTAINTIES")
        public static AbstractCard.CardRarity THE_UNCERTAINTIES;
        @SpireEnum(name = "THE_RISKS")
        public static AbstractCard.CardRarity THE_RISKS;
        @SpireEnum(name = "THE_POSSESSED")
        public static AbstractCard.CardRarity THE_POSSESSED;
    }
    
    public void smith(int level) {
        if (level < 0) return;
        if (!upgraded) upgradeName();
        ThePossessedCardSavior.loadSavior(this, level);
    }
    
    public void getSpecificID(String target) {
        this.riskID = ManiacUtils.gnrRandomTargetNumbers(target, identifiers[MathUtils.random(0, identifiers.length - 1)]);
        IDCounter++;
    }
    
    public void onObtain() {
        getSpecificID(this.cardID.substring(7) + (IDCounter % 2 == 0 ? "even" : "odd") + IDCounter);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
        logger.info("从奖励中获得：" + this.name + " 对应的ID : " + this.riskID);
    }
    
    public void usePreBattle() {
        
    }
    
    public void atBattleStart(boolean inDeck) {
        this.combatCounter = 0;
        this.counter = 0;
    }
    
    public void onExhaustOtherCard(AbstractCard card, boolean inHand) {
        
    }
    
    public void onVictoryCombat(boolean inDeck) {
        this.combatCounter = 0;
        this.counter = 0;
    }
    
    public void onMonsterDeath(AbstractMonster m, boolean inHand, boolean inDrawPile) {
        
    }

    public void onMonsterDeathInDeck(AbstractMonster m) {
        
    }
    
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target, boolean inHand) {
        
    }
    
    public float onAttackToModifyDamage(float damageAmount, DamageInfo.DamageType type, boolean inHand) {
        return damageAmount;
    }
    
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        
    }
    
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        
    }

    @Override
    public boolean canEnchant() {
        return false;
    }

    @Override
    public void enchant() {
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
