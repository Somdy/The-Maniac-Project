package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MakeRandomTypeCardInPileAction extends AbstractGameAction {
    private AbstractCard.CardType type;
    private CardGroup group;
    
    public MakeRandomTypeCardInPileAction(AbstractCreature target, AbstractCard.CardType type, CardGroup group, int amount) {
        this.target = target;
        this.type = type;
        this.group = group;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.group != null) {
                if (this.type != AbstractCard.CardType.CURSE && this.type != AbstractCard.CardType.STATUS) {
                    this.isDone = true;
                    return;
                }

                AbstractCard card;
                if (this.type == AbstractCard.CardType.CURSE) {
                    card = AbstractDungeon.getCard(AbstractCard.CardRarity.CURSE, AbstractDungeon.cardRandomRng);
                }
                else {
                    card = getStatusCards().getRandomCard(AbstractDungeon.cardRandomRng);
                }
                
                if (card != null) {
                    if (this.group == AbstractDungeon.player.drawPile) {
                        this.addToBot(new MakeTempCardInDrawPileAction(card.makeCopy(), this.amount, true, true));
                    }
                    else if (this.group == AbstractDungeon.player.discardPile) {
                        this.addToBot(new MakeTempCardInDiscardAction(card.makeCopy(), this.amount));
                    } else {
                        this.addToBot(new MakeTempCardInHandAction(card.makeCopy(), this.amount));
                    }
                }
            }
            
            this.isDone = true;
        }
    }
    
    private CardGroup getStatusCards() {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        
        group.addToTop(new Burn());
        group.addToTop(new Slimed());
        group.addToTop(new VoidCard());
        group.addToTop(new Wound());
        group.addToTop(new Dazed());
        
        return group;
    }
}
