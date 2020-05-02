package TheManiac.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;

public class GhostIgniteFireEffect extends AbstractGameEffect {
    private static final int count = 25;
    private float x;
    private float y;
    
    public GhostIgniteFireEffect(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        for (int i = 0; i < count; i++) {
            AbstractDungeon.effectsQueue.add(new GhostFireBurstParticleEffect(this.x, this.y));
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.x, this.y, Color.ROYAL));
        }
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        
    }

    @Override
    public void dispose() {

    }
}
