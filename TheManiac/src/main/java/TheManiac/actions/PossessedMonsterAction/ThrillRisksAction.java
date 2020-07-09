package TheManiac.actions.PossessedMonsterAction;

import TheManiac.cards.the_possessed.risks.AbstractRisksCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ThrillRisksAction extends AbstractGameAction {
    private AbstractPlayer player;

    public ThrillRisksAction() {
        this.player = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (!player.drawPile.isEmpty()) {
                for (AbstractCard card : player.drawPile.group) {
                    if (card instanceof AbstractRisksCard && !((AbstractRisksCard) card).isThrilled)
                        ((AbstractRisksCard) card).becomeThrill();
                }
            }

            if (!player.discardPile.isEmpty()) {
                for (AbstractCard card : player.discardPile.group) {
                    if (card instanceof AbstractRisksCard && !((AbstractRisksCard) card).isThrilled)
                        ((AbstractRisksCard) card).becomeThrill();
                }
            }

            if (!player.hand.isEmpty()) {
                for (AbstractCard card : player.hand.group) {
                    if (card instanceof AbstractRisksCard && !((AbstractRisksCard) card).isThrilled)
                        ((AbstractRisksCard) card).becomeThrill();
                }
            }
        }
        
        this.tickDuration();
    }
}
