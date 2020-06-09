package TheManiac.powers;

import TheManiac.vfx.FlashManiacAtkEffect;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DecayPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:DecayPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public DecayPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadImg("Decay");
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount > 0 && !this.owner.isDeadOrEscaped()) {
            AbstractDungeon.effectList.add(new FlashManiacAtkEffect(this.owner.hb.cX, this.owner.hb.cY, "DECAY", false));
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount <= 1) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DecayPower(owner, amount);
    }
}
