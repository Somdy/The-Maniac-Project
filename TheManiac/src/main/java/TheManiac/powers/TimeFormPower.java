package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class TimeFormPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:TimeFormPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/TimeFormPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/TimeFormPower.png";
    public boolean hasTimeWarper = false;
    public int cardsRequire;

    public TimeFormPower(int cardsToPlay, int cardsRequire) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = cardsToPlay;
        this.cardsRequire = cardsRequire;
        this.type = PowerType.BUFF;
        this.loadImg("TimeForm");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!hasTimeWarper) {
            flashWithoutSound();

            --this.amount;

            if (this.amount == 0) {
                this.cardsRequire++;
                this.amount = this.cardsRequire;
                playApplyPowerSfx();
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WhirlwindEffect(new Color(0F, 0.14F, 0.4F, 0.5F), false)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new TimeWarperPower()));
                hasTimeWarper = true;
            }
        }

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.player.hasPower("maniac:TimeWarperPower")) {
            hasTimeWarper = false;
        }
    }

    @Override
    public void updateDescription() {
        if (!hasTimeWarper) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
        else {
            this.description = DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new TimeFormPower(amount, cardsRequire);
    }
}
