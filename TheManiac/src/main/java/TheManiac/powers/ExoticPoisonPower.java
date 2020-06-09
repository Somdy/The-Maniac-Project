package TheManiac.powers;

import TheManiac.actions.ExoticPoisonDamageAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ExoticPoisonPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:ExoticPoisonPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/ExoticPoisonPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/ExoticPoisonPower.png";
    private AbstractCreature source;
    private boolean trueDmg;
    
    public ExoticPoisonPower(AbstractCreature target, AbstractCreature source, int amount, boolean trueDmg) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = target;
        this.source = source;
        this.amount = amount;
        if (this.amount >= 999) {
            this.amount = 999;
        }
        this.trueDmg = trueDmg;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.loadImg("ExoticPoison");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flashWithoutSound();
            this.addToBot(new ExoticPoisonDamageAction(this.owner, this.source, this.amount, "EXOTIC_POISON", this.trueDmg));
        }
    }

    @Override
    public void updateDescription() {
        if (this.trueDmg) {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExoticPoisonPower(owner, source, amount, trueDmg);
    }
}
