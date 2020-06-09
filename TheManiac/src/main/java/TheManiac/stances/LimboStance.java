package TheManiac.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

public class LimboStance extends AbstractStance {
    public static final String STANCE_ID = "maniac:Limbo";
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;
    private static final float DAMAGE_TAKEN = 1.25f;

    public LimboStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        return damage * DAMAGE_TAKEN;
    }

    @Override
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0f) {
                this.particleTimer = 0.2f;
                AbstractDungeon.effectsQueue.add(new DivinityParticleEffect());
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0f) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new StanceAuraEffect("Wrath"));
        }
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.player.currentHealth > AbstractDungeon.player.maxHealth * 0.5) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction("Neutral"));
        }
    }

    @Override
    public void update() {
        super.update();
        /*if (AbstractDungeon.player instanceof TheManiacCharacter) {
            ((TheManiacCharacter) AbstractDungeon.player).updateParticles();
        }*/
    }

    @Override
    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }

    @Override
    public void onEnterStance() {
        if (sfxId != -1L) {
            stopIdleSfx();
        }
        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.BLUE, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath"));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        
    }

    @Override
    public void onExitStance() {
        stopIdleSfx();
    }

    @Override
    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", sfxId);
            sfxId = -1L;
        }
    }
}
