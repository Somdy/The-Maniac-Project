package TheManiac.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashManiacAtkEffect extends AbstractGameEffect {
    public static TextureAtlas vfxAtlas = new TextureAtlas("maniacMod/images/vfx/maniacVfx.atlas");
    public TextureAtlas.AtlasRegion img;
    private TextureAtlas.AtlasRegion EXOTIC_POISON;
    private TextureAtlas.AtlasRegion MANIAC_SLASH;
    private TextureAtlas.AtlasRegion MANIAC_BLUNT;
    private TextureAtlas.AtlasRegion MANIAC_CLEAVE;
    private float x;
    private float y;
    private float sY;
    private float tY;
    private String effect;
    private boolean triggered;
    
    public FlashManiacAtkEffect(float x, float y, String effect, boolean mute) {
        this.triggered = false;
        this.duration = 0.6F;
        this.startingDuration = 0.6F;
        this.effect = effect;
        this.img = this.loadImage();
        if (this.img != null) {
            this.x = x - (float)this.img.packedWidth / 2.0F;
            y -= (float)this.img.packedHeight / 2.0F;
        }

        this.color = Color.WHITE.cpy();
        this.scale = Settings.scale;
        if (!mute) {
            this.playSound(effect);
        }
        this.y = y;
        this.sY = y;
        this.tY = y;
        
        EXOTIC_POISON = vfxAtlas.findRegion("exoticPoison");
        MANIAC_SLASH = vfxAtlas.findRegion("maniac_slash");
        MANIAC_BLUNT = vfxAtlas.findRegion("maniac_blunt");
        MANIAC_CLEAVE = vfxAtlas.findRegion("maniac_cleave");
    }
    
    private TextureAtlas.AtlasRegion loadImage() {
        switch (this.effect) {
            case "EXOTIC_POISON":
                return this.EXOTIC_POISON;
            case "MANIAC_SLASH":
                return this.MANIAC_SLASH;
            case "MANIAC_BLUNT":
                return this.MANIAC_BLUNT;
            case "MANIAC_CLEAVE":
                return this.MANIAC_CLEAVE;
            default:
                return null;
        }
    }
    
    private void playSound(String effect) {
        switch (effect) {
            case "EXOTIC_POISON":
                CardCrawlGame.sound.play("ATTACK_POISON");
                break;
            case "MANIAC_SLASH":
            case "MANIAC_CLEAVE":
                CardCrawlGame.sound.play("ATTACK_HEAVY");
                break;
            case "MANIAC_BLUNT":
                CardCrawlGame.sound.play("BLUNT_HEAVY");
                break;
            case "NONE":
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, 
                    (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        }
    }

    @Override
    public void dispose() {

    }
}
