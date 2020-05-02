package TheManiac.actions;

import TheManiac.character.TheManiacCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class ExceedEnchantmentAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private boolean upgraded;
    private boolean doubleEffect;
    private int energyOnUse;
    
    public ExceedEnchantmentAction(boolean freeToPlayOnce, int energyOnUse, boolean upgraded, boolean doubleEffect) {
        this.actionType = ActionType.SPECIAL;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
        this.doubleEffect = doubleEffect;
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
        
        if (doubleEffect) {
            effect *= 2;
        }
        
        if (effect > 0) {
            if (AbstractDungeon.player instanceof TheManiacCharacter) {
                ((TheManiacCharacter) AbstractDungeon.player).weaponUpgrades += effect;
                System.out.println("Play weapon upgrades gain: " + ((TheManiacCharacter) AbstractDungeon.player).weaponUpgrades);
            }

            if (!this.freeToPlayOnce) {
                AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
            }
        }
        
        this.isDone = true;
    }
}
