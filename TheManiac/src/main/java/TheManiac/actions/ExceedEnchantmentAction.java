package TheManiac.actions;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class ExceedEnchantmentAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private boolean upgraded;
    private int energyOnUse;
    
    public ExceedEnchantmentAction(boolean freeToPlayOnce, int energyOnUse, boolean upgraded) {
        this.actionType = ActionType.SPECIAL;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.energyOnUse = energyOnUse;
    }
    
    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;

        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
            AbstractDungeon.player.getRelic("Chemical X").flash();
        }
        
        if (upgraded) {
            effect += 1;
        }
        
        if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
            effect += 1;
        }
        
        if (effect > 0) {
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).canEnchant()) {
                    for (int i = 0; i < effect; i++) {
                        card.superFlash(Color.PURPLE);
                        ((AbstractManiacCard) card).enchant();
                    }
                }
            }
            if (!this.freeToPlayOnce) {
                AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
            }
        }
        
        this.isDone = true;
    }
}
