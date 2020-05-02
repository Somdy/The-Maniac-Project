package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BitingDaggerPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:BitingDaggerPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/BitingDaggerPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/BitingDaggerPower.png";
    private int powers;

    public BitingDaggerPower(int amount, int powers) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.powers = powers;
        this.type = PowerType.BUFF;
        this.canGoNegative = true;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this.owner, new WeaknessPower(target, this.amount)));
            if (this.powers > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this.owner, new BleedingPower(target, this.powers)));
            }
        }
    }

    @Override
    public void updateDescription() {
        if (this.powers > 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
        else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new BitingDaggerPower(amount, powers);
    }
}
