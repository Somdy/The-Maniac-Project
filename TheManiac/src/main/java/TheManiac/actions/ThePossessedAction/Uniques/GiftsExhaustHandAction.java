package TheManiac.actions.ThePossessedAction.Uniques;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GiftsExhaustHandAction extends AbstractGameAction {
    private AbstractPlayer player;
    
    public GiftsExhaustHandAction() {
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            int num = this.player.hand.group.size();
            
            for (AbstractCard card : this.player.hand.group) {
                if (Settings.FAST_MODE) {
                    this.addToTop(new ExhaustAction(1, true, true, false, Settings.ACTION_DUR_XFAST));
                } else {
                    this.addToTop(new ExhaustAction(1, true, true));
                }
            }
            
            for (int i = 0; i < num; i++) {
                AbstractCard card = AbstractDungeon.returnTrulyRandomCard();
                while (card == null || card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS) {
                    card = AbstractDungeon.returnTrulyRandomCardInCombat();
                }
                card.modifyCostForCombat(-9);
                this.addToBot(new MakeTempCardInHandAction(card.makeStatEquivalentCopy(), 1));
            }
        }
        
        this.tickDuration();
    }
}
