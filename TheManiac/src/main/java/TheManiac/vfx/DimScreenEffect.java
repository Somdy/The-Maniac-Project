package TheManiac.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DimScreenEffect extends AbstractGameEffect {
    private boolean lighten = false;
    
    public DimScreenEffect() {
        this.duration = 1.0F;
        this.color = Color.BLACK.cpy();
    }

    @Override
    public void update() {
        float targetAlpha = 0.95F;
        this.duration -= Gdx.graphics.getDeltaTime();

        if (lighten) {
            this.color.a = targetAlpha * this.duration;
            if (this.duration <= 0.0F) {
                this.color.a = 0.0F;
                this.isDone = true;
            }
        } else {
            this.color.a = targetAlpha * (1.0F - this.duration);
            if (this.duration <= 0.0F) {
                this.color.a = targetAlpha;
            }
        }
    }

    public void lighten() {
        lighten = true;
        this.duration = 1F;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
    }

    @Override
    public void dispose() {

    }
}
