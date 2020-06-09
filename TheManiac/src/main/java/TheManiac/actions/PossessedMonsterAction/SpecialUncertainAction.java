package TheManiac.actions.PossessedMonsterAction;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpecialUncertainAction extends AbstractGameAction {
    private AbstractPlayer player = AbstractDungeon.player;
    private int opt;
    
    public SpecialUncertainAction(int opt, int magics) {
        this.opt = opt;
        this.amount = magics;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            switch (opt) {
                case 1:
                    modifyCosts();
                    break;
                case 2:
                    
            }
        }
        
        this.tickDuration();
    }
    
    private void modifyCosts() {
        if (!player.drawPile.isEmpty()) {
            for (AbstractCard card : player.drawPile.group) {
                int newCost = AbstractDungeon.cardRandomRng.random(-1, 1);
                card.modifyCostForCombat(newCost);
            }
        }
        if (!player.discardPile.isEmpty()) {
            for (AbstractCard card : player.discardPile.group) {
                int newCost = AbstractDungeon.cardRandomRng.random(-1, 1);
                card.modifyCostForCombat(newCost);
            }
        }
    }
}
