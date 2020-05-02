package TheManiac.orbs;

import TheManiac.actions.DoublestrikeSphereAction;
import TheManiac.powers.AntiquityPower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.*;

public class DoublestrikeSphere extends AbstractManiacOrb {
    public static final String ORB_ID = "maniac:DoublestrikeSphere";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    private static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/orbs/doublestrike_sphere.png";
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;
    private static final int damage = 0;
    private static final int damageEvoke = 2;

    public DoublestrikeSphere() {
        this.ID = ORB_ID;
        this.name = NAME;
        this.img = ImageMaster.loadImage(IMG_PATH);
        this.evokeAmount = this.baseEvokeAmount = damageEvoke;
        this.passiveAmount = this.basePassiveAmount = damage;
        this.updateDescription();
        this.angle = MathUtils.random(360.0f);
        this.channelAnimTimer = 0.5f;
    }

    public void setSphereValues(int passiveDamage, int evokeDamage) {
        this.passiveAmount = this.basePassiveAmount += passiveDamage;
        this.evokeAmount = this.baseEvokeAmount += evokeDamage;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        applyAntiquity();
        applyFocus();
        this.description = DESCRIPTION[0] + this.passiveAmount + DESCRIPTION[1] + this.evokeAmount + DESCRIPTION[2];
    }

    @Override
    public void onEvoke() {
        for (int i = 0; i < 2; i++) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new CleaveEffect(), 0.1F));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player,
                    DamageInfo.createDamageMatrix(this.evokeAmount, true, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
        }
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
        }
        else {
            this.passiveAmount = this.basePassiveAmount;
            this.evokeAmount = this.baseEvokeAmount;
        }
    }

    @Override
    public void onEndOfTurn() {
        float speedTime = 0.4F / AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) { speedTime = 0.0F; }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.LIGHTNING), speedTime));
        AbstractDungeon.actionManager.addToBottom(new DoublestrikeSphereAction(new DamageInfo(AbstractDungeon.player, this.passiveAmount, DamageInfo.DamageType.THORNS), this));
    }

    @Override
    public AbstractOrb makeCopy() {
        return new DoublestrikeSphere();
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
    public void updateAnimation() {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 45.0f;
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new LightningOrbPassiveEffect(cX, cY));
            this.vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    @Override
    public void triggerEvokeAnimation() {
        AbstractDungeon.effectsQueue.add(new LightningOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }
}
