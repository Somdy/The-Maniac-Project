package TheManiac.actions.ThePossessedAction;

import TheManiac.cards.status.Unidentified;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnidentifiedAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(UnidentifiedAction.class.getName());
    private float chance;
    private AbstractCard targetCard;
    private AbstractPlayer p;
    private AbstractMonster m;

    public UnidentifiedAction(float chance, AbstractPlayer p, AbstractMonster m) {
        this.chance = chance;
        this.p = p;
        this.m = m;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.cardRandomRng.randomBoolean(chance)) {
                targetCard = AbstractDungeon.curseCardPool.getRandomCard(true);
                this.addToBot(new MakeTempCardInDiscardAction(targetCard, 1));
                this.isDone = true;
                return;
            }
            
            returnTrulyRandomColorCard(p, m);
            
            if (canUseCard(targetCard, p, m)) {
                logger.info("将要打出：" + targetCard.name);
                targetCard.use(p, m);
            } else {
                this.addToBot(new UnidentifiedAction(this.chance, p, m));
            }
        }
        
        this.tickDuration();
    }
    
    private void returnTrulyRandomColorCard(AbstractPlayer p, AbstractMonster m) {
        targetCard = CardLibrary.getAnyColorCard(getRandomRarity());
        
        while (!canUseCard(targetCard, p, m) || targetCard.cardID.equals(Unidentified.ID)) {
            logger.info(targetCard.name + "无法被打出，重选。");
            targetCard = CardLibrary.getAnyColorCard(getRandomRarity());
        }
    }
    
    private AbstractCard.CardRarity getRandomRarity() {
        AbstractCard.CardRarity rarity = AbstractCard.CardRarity.COMMON;
        int num = AbstractDungeon.cardRandomRng.random(99);
        
        if (num < 17)
            return AbstractCard.CardRarity.RARE;
        else if (num < 59)
            return AbstractCard.CardRarity.UNCOMMON;
        else if (num < 71)
            return AbstractCard.CardRarity.COMMON;
        else return AbstractCard.CardRarity.BASIC;
    }
    
    private boolean canUseCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        if (card.canUse(p, m)) return true;

        if (card.type == AbstractCard.CardType.STATUS && card.costForTurn < -1 &&
                !AbstractDungeon.player.hasRelic("Medical Kit")) {
            return false;
        }

        if (card.type == AbstractCard.CardType.CURSE && card.costForTurn < -1 &&
                !AbstractDungeon.player.hasRelic("Blue Candle")) {
            return false;
        }

        return card.cardPlayable(m);
    }
}
