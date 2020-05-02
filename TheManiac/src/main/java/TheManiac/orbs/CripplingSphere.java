package TheManiac.orbs;

import TheManiac.actions.CripplingSphereAction;
import TheManiac.powers.AntiquityPower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

public class CripplingSphere extends AbstractManiacOrb {
    public static final String ORB_ID = "maniac:CripplingSphere";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    private static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/orbs/weakenstrike_sphere.png";
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;
    private static final int damage = 1;
    private static final int damageEvoke = 2;
    private static final int weakness = 1;
    private static final int bleeding = 2;

    public CripplingSphere() {
        this.ID = ORB_ID;
        this.name = NAME;
        this.img = ImageMaster.loadImage(IMG_PATH);
        this.evokeAmount = this.baseEvokeAmount = damageEvoke;
        this.passiveAmount = this.basePassiveAmount = damage;
        this.passivePowersAmt = this.basePassivePowersAmt = weakness;
        this.evokeAdditionalAmt = this.baseEvokeAdditionalAmt = bleeding;
        this.updateDescription();
        this.angle = MathUtils.random(360.0f);
        this.channelAnimTimer = 0.5f;
    }

    public void setSphereValues(int passiveDamage, int evokeDamage, int passivePowers, int evokePowers) {
        this.evokeAmount = this.baseEvokeAmount += passiveDamage;
        this.passiveAmount = this.basePassiveAmount += evokeDamage;
        this.passivePowersAmt = this.basePassivePowersAmt += passivePowers;
        this.evokeAdditionalAmt = this.baseEvokeAdditionalAmt += evokePowers;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        applyFocus();
        applyAntiquity();
        this.description = DESCRIPTION[0] + this.passiveAmount +DESCRIPTION[1] + this.passivePowersAmt + DESCRIPTION[2] + this.evokeAmount
                + DESCRIPTION[3] + this.evokeAdditionalAmt + DESCRIPTION[4];
    }

    @Override
    public void applyFocus() {
        this.evokeAmount = this.baseEvokeAmount;
        this.passiveAmount = this.basePassiveAmount;
        this.evokeAdditionalAmt = this.baseEvokeAdditionalAmt;
    }

    @Override
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

    @Override
    public void onEvoke() {
        float speedTime = 0.6F / AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) { speedTime = 0.0F; }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.DARK), speedTime));
        AbstractDungeon.actionManager.addToBottom(new CripplingSphereAction(new DamageInfo(AbstractDungeon.player, this.evokeAmount, DamageInfo.DamageType.THORNS), this, true, this.evokeAdditionalAmt));
    }

    @Override
    public void onEndOfTurn() {
        float speedTime = 0.6F / AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) { speedTime = 0.0F; }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.DARK), speedTime));
        AbstractDungeon.actionManager.addToBottom(new CripplingSphereAction(new DamageInfo(AbstractDungeon.player, this.passiveAmount, DamageInfo.DamageType.THORNS), this, false, this.passivePowersAmt));
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CripplingSphere();
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 45.0f;
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new DarkOrbPassiveEffect(cX, cY));
            this.vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setColor(new Color(1.0f, 1.0f, 1.0f, c.a / 2.0f));
        spriteBatch.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, scale, angle, 0, 0, 96, 96, false, false);
        spriteBatch.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
        spriteBatch.setBlendFunction(770, 1);
        spriteBatch.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, -angle, 0, 0, 96, 96, false, false);
        spriteBatch.setBlendFunction(770, 771);
        renderText(spriteBatch);
        hb.render(spriteBatch);
    }

    @Override
    public void triggerEvokeAnimation() {
        CardCrawlGame.sound.play("ORB_DARK_EVOKE", 0.1F);
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.cX, this.cY));
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.1F);
    }
}
