package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class TimeWarperPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:TimeWarperPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/TimeWarperPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/TimeWarperPower.png";
    private static final Color MANIAC_BLUE = new Color(0f, 0.14f, 0.4f, 0.5f);

    public TimeWarperPower() {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.loadImg("TimeWarper");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        playApplyPowerSfx();
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new WhirlwindEffect(new Color(0F, 0.14F, 0.4F, 1.0F), true)));
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(MANIAC_BLUE, true));
        AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
        AbstractDungeon.actionManager.addToBottom(new SkipEnemiesTurnAction());
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new TimeWarperPower();
    }
}
