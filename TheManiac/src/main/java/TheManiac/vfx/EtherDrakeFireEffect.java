package TheManiac.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class EtherDrakeFireEffect extends AbstractGameEffect {
    private float timer = 0.0F;
    private static final float INTERVAL = 0.05F;
    
    public EtherDrakeFireEffect() {
        this.duration = 3.0F;
        this.startingDuration = this.duration;
    }

    @Override
    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.play("GHOST_FLAMES");
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.FIREBRICK));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            AbstractDungeon.effectsQueue.add(new BetterGiantFireEffect(MathUtils.random(0.89F, 1F), MathUtils.random(0.15F, 0.53F), MathUtils.random(0F, 0.28F), 0F));
            AbstractDungeon.effectsQueue.add(new BetterGiantFireEffect(MathUtils.random(0.89F, 1F), MathUtils.random(0.15F, 0.53F), MathUtils.random(0F, 0.28F), 0F));
            this.timer = 0.03F;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        
    }

    @Override
    public void dispose() {

    }
}
