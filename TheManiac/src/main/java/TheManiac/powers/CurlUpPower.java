package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class CurlUpPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:CurlUpPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/CurlUpPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/CurlUpPower.png";
    private boolean triggered = false;
    private int powers;
    
    public CurlUpPower(AbstractCreature owner, int amount, int powers) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.powers = powers;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!this.triggered && damageAmount < this.owner.currentHealth && damageAmount > 0 && info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.triggered = true;
            this.addToBot(new ChangeStateAction((AbstractMonster)this.owner, "CLOSED"));
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new PlatedArmorPower(this.owner, this.powers)));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.powers + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CurlUpPower(owner, amount, powers);
    }
}
