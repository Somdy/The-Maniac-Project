package TheManiac.vfx;

import TheManiac.helper.ManiacImageMaster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlashManiacAtkEffect extends AbstractGameEffect {
    private static final Logger logger = LogManager.getLogger(FlashManiacAtkEffect.class.getName());
    public static TextureAtlas vfxAtlas = new TextureAtlas(Gdx.files.internal("maniacMod/images/vfx/maniacVfx.atlas"));
    public TextureAtlas.AtlasRegion img;
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
        } else {
            logger.info("Unable to find " + this.effect + "'s image!!!");
        }

        this.color = Color.WHITE.cpy();
        this.scale = Settings.scale;
        if (!mute) {
            this.playSound(effect);
        }
        this.y = y;
        this.sY = y;
        this.tY = y;
    }
    
    private TextureAtlas.AtlasRegion loadImage() {
        switch (this.effect) {
            case "EXOTIC_POISON":
                return ManiacImageMaster.EXOTIC_POISON;
            case "PLAGUE":
                return ManiacImageMaster.PLAGUE;
            case "DECAY":
                return ManiacImageMaster.DECAY;
            case "MANIAC_SLASH":
                return ManiacImageMaster.MANIAC_SLASH;
            case "MANIAC_BLUNT":
                return ManiacImageMaster.MANIAC_BLUNT;
            case "MANIAC_CLEAVE":
                return ManiacImageMaster.MANIAC_CLEAVE;
            default:
                return null;
        }
    }
    
    private void playSound(String effect) {
        switch (effect) {
            case "PLAGUE":
            case "DECAY":    
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
    public void update() {
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, 
                    (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
            logger.info("Succeeded in updating " + this.effect + " attack effects!");
        }
    }

    @Override
    public void dispose() {

    }
}
