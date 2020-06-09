package TheManiac.cards.the_possessed;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
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
    public int combatCounter;
    public int counter;
    
    public ManiacRisksCard(String id, String img, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, CardCrawlGame.languagePack.getCardStrings(id).NAME, img, cost, CardCrawlGame.languagePack.getCardStrings(id).DESCRIPTION, type, TheManiacCharacter.Enums.MANIAC_POSSESSED, rarity, target);
        
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
    
    public void onObtain() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
        logger.info("从奖励中获得：" + this.name);
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
    
    public int onAttackedToModifyDamage(DamageInfo info, int damageAmount, boolean inHand) {
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
