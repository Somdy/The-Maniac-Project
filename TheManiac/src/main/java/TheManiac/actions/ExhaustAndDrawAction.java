package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ExhaustAndDrawAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:ExhaustAndDrawAction");
    public static final String[] TEXT = uiStrings.TEXT;
    
    public ExhaustAndDrawAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], amount, true, true);
            this.addToBot(new WaitAction(0.25F));
        }
        else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            AbstractPlayer p = AbstractDungeon.player;
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                int drawAmt = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    p.hand.moveToExhaustPile(c);
                    c.triggerOnExhaust();
                }

                this.addToBot(new DrawCardAction(p, drawAmt));
            }
            
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        
        this.tickDuration();
    }
}