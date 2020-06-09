package TheManiac.vfx.PossessedVfx;

import TheManiac.vfx.GhostFireBurstParticleEffect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;

public class SpectreIgniteFireEffect extends AbstractGameEffect {
    private static final int count = 25;
    private float x;
    private float y;
    private Color color;
    
    public SpectreIgniteFireEffect(float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void update() {
        for (int i = 0; i < count; i++) {
            AbstractDungeon.effectsQueue.add(new GhostFireBurstParticleEffect(this.x, this.y));
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.x, this.y, color));
        }
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        
    }

    @Override
    public void dispose() {

    }
}
