package TheManiac.actions.ThePossessedAction;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BrokenMagnetAction extends AbstractGameAction {
    private float chance;
    
    public BrokenMagnetAction(int amount, float chance) {
        this.amount = amount;
        this.chance = chance;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.cardRandomRng.randomBoolean(this.chance)) {
                AbstractCard curse = AbstractDungeon.curseCardPool.getRandomCard(true);
                this.addToBot(new MakeTempCardInHandAction(curse.makeCopy(), this.amount));
            } else {
                AbstractCard card = AbstractDungeon.returnTrulyRandomCard();
                card.upgrade();
                card.modifyCostForCombat(-9);
                card.isCostModified = true;
                this.addToBot(new MakeTempCardInHandAction(card.makeStatEquivalentCopy(), this.amount));
            }
        }
        
        this.tickDuration();
    }
}
