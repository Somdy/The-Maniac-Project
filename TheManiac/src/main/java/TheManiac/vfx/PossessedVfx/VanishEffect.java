package TheManiac.vfx.PossessedVfx;

import TheManiac.helper.ManiacImageMaster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

public class VanishEffect extends AbstractGameEffect {
    public float x;
    public float y;
    public Color color;
    
    
    public VanishEffect(AbstractCreature creature, Color color) {
        this.x = creature.hb.cX;
        this.y = creature.hb.cY;
        this.color = color;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.play("ATTACK_FIRE");
        for (int i = 0; i < 70; i++) {
            AbstractDungeon.effectsQueue.add(new VanishParticleEffect(this.x, this.y, ManiacImageMaster.ColorDeviator(color, 0.25F, 0.45F, true)));
        }
        for (int i = 0; i < 20; i++) {
            AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.x, this.y));
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
