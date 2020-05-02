package TheManiac.orbs;

import TheManiac.powers.AntiquityPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractManiacOrb extends AbstractOrb {
    public int evokeAdditionalAmt = 0;
    public int baseEvokeAdditionalAmt = 0;
    public int passivePowersAmt = 0;
    public int basePassivePowersAmt = 0;

    public AbstractManiacOrb() {

    }

    public void applyAntiquity() {
        AbstractPower power = AbstractDungeon.player.getPower(AntiquityPower.POWER_ID);
        if (power != null) {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
            this.evokeAmount = Math.max(0, this.baseEvokeAmount + power.amount);
            this.evokeAdditionalAmt = Math.max(0, this.baseEvokeAdditionalAmt + (power.amount / 2));
            this.passivePowersAmt = Math.max(0, this.baseEvokeAdditionalAmt + (power.amount / 2));
        }
        else {
            this.passiveAmount = this.basePassiveAmount;
            this.evokeAmount = this.baseEvokeAmount;
            this.evokeAdditionalAmt = this.baseEvokeAdditionalAmt;
            this.passivePowersAmt = this.basePassivePowersAmt;
        }
    }

}
