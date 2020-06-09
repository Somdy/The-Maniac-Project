package TheManiac.actions.PossessedMonsterAction;

import TheManiac.cards.status.Unidentified;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RiskySunglassessAction extends AbstractGameAction {
    private int times;
    
    public RiskySunglassessAction(int chance, int times) {
        this.amount = chance;
        this.times = times;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            if (amount < 0) {
                this.isDone = true;
                return;
            }
            
            int chance = AbstractDungeon.aiRng.random(amount);
            
            if (chance < amount * 0.25F) {
                int amounts = this.times / 2;
                if (amounts <= 1)
                    amounts = 2;
                for (int i = 0; i < amounts; i++) {
                    AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.CURSE);
                    card.isFlipped = true;
                    this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
                    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                        if (c == card.makeStatEquivalentCopy()) {
                            c.isFlipped = true;
                        }
                    }
                }
            }
            else if (chance < amount * 0.65F) {
                if (!AbstractDungeon.player.drawPile.isEmpty()) {
                    for (int i = 0; i < this.times; i++) {
                        AbstractCard card = AbstractDungeon.player.drawPile.getRandomCard(true);
                        card.rawDescription = "?";
                        card.initializeDescription();
                    }
                }
                else if (!AbstractDungeon.player.discardPile.isEmpty()) {
                    for (int i = 0; i < this.times; i++) {
                        AbstractCard card = AbstractDungeon.player.discardPile.getRandomCard(true);
                        card.rawDescription = "?";
                        card.initializeDescription();
                    }
                }
                else if (!AbstractDungeon.player.hand.isEmpty()) {
                    for (int i = 0; i < this.times; i++) {
                        AbstractCard card = AbstractDungeon.player.hand.getRandomCard(true);
                        card.rawDescription = "?";
                        card.initializeDescription();
                    }
                } else {
                    System.out.println("你TMD一张牌都没有的吗？？？");
                }
            } else {
                int amounts = this.times / 2;
                if (amounts <= 2)
                    amounts = 3;
                for (int i = 0; i < amounts; i++) {
                    Unidentified unidentified = new Unidentified();
                    this.addToBot(new MakeTempCardInDrawPileAction(unidentified, 1, true, true));
                }
            }
        }
        
        this.tickDuration();
    }
}
