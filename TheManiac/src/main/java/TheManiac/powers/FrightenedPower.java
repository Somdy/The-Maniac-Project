package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FrightenedPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:FrightenedPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/FrightenedPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/FrightenedPower.png";
    private static final float DAMAGE_REDUCE = 0.15f;
    private static final float DAMAGE_RECEIVE = 0.15f;

    public FrightenedPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.owner != null && this.amount > 0 ) {
            return damage * (1 - DAMAGE_REDUCE);
        }
        return damage;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.owner != null && this.amount > 0 ) {
            return damage * (1 + DAMAGE_RECEIVE);
        }
        return damage;
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount > 0) {
            this.amount--;
        }
        if (this.amount == 0 || this.amount < 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DAMAGE_REDUCE * 100 + DESCRIPTIONS[1] + DAMAGE_RECEIVE * 100 + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FrightenedPower(owner, amount);
    }
}
