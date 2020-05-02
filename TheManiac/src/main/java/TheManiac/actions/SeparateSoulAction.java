package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SeparateSoulAction extends AbstractGameAction {
    private AbstractPlayer player;
    private AbstractCard.CardType[] types = {AbstractCard.CardType.ATTACK, AbstractCard.CardType.SKILL, AbstractCard.CardType.POWER};

    public SeparateSoulAction(int amount) {
        this.amount = amount;
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (player.masterDeck.isEmpty() || this.amount <= 0) {
                this.isDone = true;
                return;
            } else {
                int count = 0;
                for (AbstractCard card : player.masterDeck.group) {
                    if (card != null) {
                        count++;
                    }
                }

                if (count != 0) {
                    int index = AbstractDungeon.aiRng.random(0, types.length - 1);
                    AbstractCard card = player.masterDeck.getRandomCard(types[index], true);
                    
                    if (card != null) {
                        AbstractCard copy = card.makeSameInstanceOf();

                        this.addToBot(new MakeTempCardInDrawPileAction(copy.makeSameInstanceOf(), this.amount, true, true));
                    }
                }
            }
        }
        
        this.isDone = true;
    }
}
