package TheManiac.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class GhostFireEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private static final float DUR = 1.0F;
    
    public GhostFireEffect(float x, float y) {
        this.img = getImg();
        this.x = x + MathUtils.random(-2.0F, 2.0F) * Settings.scale - this.img.packedWidth / 2.0F;
        this.y = y + MathUtils.random(-2.0F, 2.0F) * Settings.scale - this.img.packedHeight / 2.0F;
        this.vX = MathUtils.random(-10.0F, 10.0F) * Settings.scale;
        this.vY = MathUtils.random(20.0F, 150.0F) * Settings.scale;
        this.duration = 1.0F;
        this.color = Color.SCARLET.cpy();
        this.color.a = 0.0F;
        this.scale = Settings.scale * MathUtils.random(5.0F, 6.0F);
    }

    private TextureAtlas.AtlasRegion getImg() {
        switch (MathUtils.random(0, 2)) {
            case 0:
                return ImageMaster.TORCH_FIRE_1;
            case 1:
                return ImageMaster.TORCH_FIRE_2;
        }
        return ImageMaster.TORCH_FIRE_3;
    }

    @Override
    public void update() {
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.scale > 0.1F) {
            this.scale -= Gdx.graphics.getDeltaTime() / 4.0F;
        }
        this.color.a = this.duration / 2.0F;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, 
                this.img.packedHeight, this.scale * MathUtils.random(0.95F, 1.05F), this.scale * MathUtils.random(0.95F, 1.05F), this.rotation);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }
}
