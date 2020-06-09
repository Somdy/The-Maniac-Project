package TheManiac.vfx.PossessedVfx;

import TheManiac.helper.ManiacImageMaster;
import TheManiac.vfx.GhostFireBurstParticleEffect;
import TheManiac.vfx.GhostIgniteFireEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GhostlyWeakFireEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;

public class SpectreFireBallEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float startX;
    private float startY;
    private float targetX;
    private float targetY;
    private float vfxTimer = 0.0F;
    private Color color;
    
    public SpectreFireBallEffect(float startX, float startY, float targetX, float targetY, Color color) {
        this.startingDuration = 0.5F;
        this.duration = 0.5F;
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX + MathUtils.random(-20.0F, 20.0F) * Settings.scale;
        this.targetY = targetY + MathUtils.random(-20.0F, 20.0F) * Settings.scale;
        this.x = startX;
        this.y = startY;
        this.color = color;
    }

    @Override
    public void update() {
        this.x = Interpolation.fade.apply(this.targetX, this.startX, this.duration / this.startingDuration);
        this.y = Interpolation.fade.apply(this.targetY, this.startY, this.duration / this.startingDuration);
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            this.vfxTimer = 0.016F;
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.x, this.y, color));
            AbstractDungeon.effectsQueue.add(new GhostFireBurstParticleEffect(this.x, this.y));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            AbstractDungeon.effectsQueue.add(new SpectreIgniteFireEffect(this.x, this.y, ManiacImageMaster.ColorDeviator(color, 0.1F, 0.3F)));
            AbstractDungeon.effectsQueue.add(new GhostlyWeakFireEffect(this.x, this.y));
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        
    }

    @Override
    public void dispose() {

    }
}
