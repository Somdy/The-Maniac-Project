package TheManiac.actions;

import TheManiac.orbs.CripplingSphere;
import TheManiac.orbs.DoublestrikeSphere;
import TheManiac.orbs.ShieldSphere;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SphereChannelAction extends AbstractGameAction {
    private int passiveAmount;
    private int evokeAmount;
    private int additionalEvoke;
    private int additionalPassive;

    public SphereChannelAction(int passiveAmount, int evokeAmount, int additionalEvoke, int additionalPassive) {
        this.passiveAmount = passiveAmount;
        this.evokeAmount = evokeAmount;
        this.additionalEvoke = additionalEvoke;
        this.additionalPassive = additionalPassive;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {

            ShieldSphere shield = new ShieldSphere();
            shield.setSphereValues(this.passiveAmount, this.evokeAmount, this.additionalEvoke);

            DoublestrikeSphere doublestrike = new DoublestrikeSphere();
            doublestrike.setSphereValues(this.passiveAmount, this.evokeAmount);

            CripplingSphere crippling = new CripplingSphere();
            crippling.setSphereValues(this.passiveAmount, this.evokeAmount, this.additionalPassive, this.additionalEvoke);

            AbstractDungeon.actionManager.addToBottom(new ChannelAction(shield));
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(doublestrike));
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(crippling));
        }

        this.isDone = true;

    }
}
