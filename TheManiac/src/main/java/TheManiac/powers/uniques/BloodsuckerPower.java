package TheManiac.powers.uniques;

import TheManiac.TheManiac;
import TheManiac.powers.AbstractManiacPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BloodsuckerPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheManiac.makeID("BloodsuckerPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public BloodsuckerPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.loadImg("Bloodsucker");
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && target != null) {
            this.flash();
            this.addToBot(new HealAction(this.owner, this.owner, damageAmount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BloodsuckerPower(owner);
    }
}
