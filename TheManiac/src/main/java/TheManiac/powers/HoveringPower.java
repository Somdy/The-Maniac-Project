package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HoveringPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:HoveringPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/HoveringPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/HoveringPower.png";
    
    public HoveringPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.loadImg("Hovering");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_FLIGHT", 0.05F);
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return (int)(damage * 0.65);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.flash();
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + 35 + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new HoveringPower(owner);
    }
}
