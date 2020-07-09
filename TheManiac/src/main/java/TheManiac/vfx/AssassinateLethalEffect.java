package TheManiac.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;

public class AssassinateLethalEffect extends AbstractGameEffect {
    private float x;
    private float y;

    public AssassinateLethalEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.startingDuration = 0.1F;
        this.duration = this.startingDuration;
    }

    public void update() {
        CardCrawlGame.sound.playA("ATTACK_WHIFF_1", 0.4F);
        CardCrawlGame.sound.playA("ATTACK_IRON_1", -0.1F);
        CardCrawlGame.sound.playA("ATTACK_IRON_3", -0.1F);
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(this.x, this.y - 30.0F * Settings.scale, -500.0F, -500.0F, 135.0F, 5.0F, Color.SCARLET, Color.SCARLET));
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(this.x, this.y - 30.0F * Settings.scale, 500.0F, -500.0F, 225.0F, 5.0F, Color.SCARLET, Color.SCARLET));

        for(int i = 0; i < 15; ++i) {
            AbstractDungeon.effectsQueue.add(new UpgradeShineParticleEffect(this.x + MathUtils.random(-40.0F, 40.0F) * Settings.scale, this.y + MathUtils.random(-40.0F, 40.0F) * Settings.scale));
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
