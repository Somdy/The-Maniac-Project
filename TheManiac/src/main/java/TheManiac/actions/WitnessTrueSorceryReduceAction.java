package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class WitnessTrueSorceryReduceAction extends AbstractGameAction {
    private AbstractPlayer p;

    public WitnessTrueSorceryReduceAction(int reduceAmount) {
        this.target = AbstractDungeon.player;
        this.p = AbstractDungeon.player;
        this.amount = reduceAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> cardList = p.hand.group;
            Iterator var = cardList.iterator();
            
            while (var.hasNext()) {
                AbstractCard card = (AbstractCard)var.next();
                if (card.costForTurn > this.amount) {
                    card.costForTurn -= this.amount;
                    card.isCostModifiedForTurn = true;
                }
            }
        }

        this.tickDuration();
    }
}
