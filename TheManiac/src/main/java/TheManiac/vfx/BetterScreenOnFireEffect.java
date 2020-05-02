package TheManiac.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;

public class BetterScreenOnFireEffect extends AbstractGameEffect {
    private Color color;
    private float timer = 0.0F;
    private static final float INTERVAL = 0.05F;
    
    public BetterScreenOnFireEffect(Color color) {
        this.color = color;
        this.duration = 3.0F;
        this.startingDuration = this.duration;
    }

    @Override
    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.play("GHOST_FLAMES");
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(this.color));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
            this.timer = 0.05F;
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
