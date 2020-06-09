package TheManiac.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManiacImageMaster {
    private static final Logger logger = LogManager.getLogger(ManiacImageMaster.class.getName());
    
    public static TextureAtlas vfxAtlas;
    public static TextureAtlas cardAtlas;
    
    public static TextureAtlas.AtlasRegion EXOTIC_POISON;
    public static TextureAtlas.AtlasRegion PLAGUE;
    public static TextureAtlas.AtlasRegion DECAY;
    public static TextureAtlas.AtlasRegion MANIAC_BLUNT;
    public static TextureAtlas.AtlasRegion MANIAC_SLASH;
    public static TextureAtlas.AtlasRegion MANIAC_CLEAVE;
    public static TextureAtlas.AtlasRegion VIOLENT_FIRE;
    public static TextureAtlas.AtlasRegion ORANGE_FIRE;
    public static TextureAtlas.AtlasRegion SHADOW_COVER_SMALL;
    public static TextureAtlas.AtlasRegion SHADOW_COVER_LARGE;
    public static TextureAtlas.AtlasRegion BLIND_PORTRAIT;
    
    public static Texture MIXES_REWARD;
    public static Texture SHINIES_REWARD;
    public static Texture UNCERTAINTIES_REWARD;
    public static Texture RISKS_REWARD;
    public static Texture POSSES_REWARD;
    public static Texture RELICS_REWARD;
    
    public static Texture INTENT_ATK_MINION_1;
    public static Texture INTENT_ATK_MINION_2;
    public static Texture INTENT_ATK_MINION_3;
    public static Texture INTENT_ATK_MINION_4;
    public static Texture INTENT_ATK_MINION_5;
    public static Texture INTENT_ATK_MINION_6;
    public static Texture INTENT_ATK_MINION_7;
    
    
    public static void Initialize() {
        long startingTime = System.currentTimeMillis();
        logger.info("===正在载入一堆新的图片");
        logger.info("==正在载入新的特效图片");
        vfxAtlas = new TextureAtlas(Gdx.files.internal("maniacMod/images/vfx/maniacVfx.atlas"));
        EXOTIC_POISON = vfxAtlas.findRegion("exoticPoison");
        PLAGUE = vfxAtlas.findRegion("plague");
        DECAY = vfxAtlas.findRegion("decay");
        MANIAC_BLUNT = vfxAtlas.findRegion("maniac_blunt");
        MANIAC_SLASH = vfxAtlas.findRegion("maniac_slash");
        MANIAC_CLEAVE = vfxAtlas.findRegion("maniac_cleave");
        VIOLENT_FIRE = vfxAtlas.findRegion("violent_fire");
        ORANGE_FIRE = vfxAtlas.findRegion("orange_fire");
        logger.info("已载入所有新特效的图片==");
        logger.info("==正在载入杂七杂八的图片");
        MIXES_REWARD = ImageMaster.loadImage("maniacMod/images/rewards/MixesIcon.png");
        SHINIES_REWARD = ImageMaster.loadImage("maniacMod/images/rewards/ShiniesIcon.png");
        UNCERTAINTIES_REWARD = ImageMaster.loadImage("maniacMod/images/rewards/UncertaintiesIcon.png");
        RISKS_REWARD = ImageMaster.loadImage("maniacMod/images/rewards/RisksIcon.png");
        POSSES_REWARD = ImageMaster.loadImage("maniacMod/images/rewards/PossessionsIcon.png");
        RELICS_REWARD = ImageMaster.loadImage("maniacMod/images/rewards/RelicsIcon.png");
        SHADOW_COVER_SMALL = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("maniacMod/images/512defaults/outlook/shadow_back.png"), 0, 0, 512, 512);
        SHADOW_COVER_LARGE = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("maniacMod/images/1024portraits/cardui/shadow_back.png"), 0, 0, 1024, 1024);
        BLIND_PORTRAIT = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("maniacMod/images/512defaults/outlook/blindCard.png"), 0, 0, 250, 190);
        INTENT_ATK_MINION_1 = new Texture("maniacMod/images/intents/attack_minion_1.png");
        INTENT_ATK_MINION_2 = new Texture("maniacMod/images/intents/attack_minion_2.png");
        INTENT_ATK_MINION_3 = new Texture("maniacMod/images/intents/attack_minion_3.png");
        INTENT_ATK_MINION_4 = new Texture("maniacMod/images/intents/attack_minion_4.png");
        INTENT_ATK_MINION_5 = new Texture("maniacMod/images/intents/attack_minion_5.png");
        INTENT_ATK_MINION_6 = new Texture("maniacMod/images/intents/attack_minion_6.png");
        INTENT_ATK_MINION_7 = new Texture("maniacMod/images/intents/attack_minion_7.png");
        logger.info("已载入杂七杂八的新图片==");
        logger.info("已载入几乎所有新的图片===用时：" + (System.currentTimeMillis() - startingTime) + "ms");
    }
    
    public static Color ColorDeviator(Color baseColor, float minRange, float maxRange, boolean trans, boolean keepBright, boolean keepSaturated) {
        Color newColor;
        
        if (maxRange > 1)
            maxRange = MathUtils.random(0, 1);
        if (minRange > 1)
            minRange = MathUtils.random(0, maxRange);
        
        float diff = MathUtils.clamp(MathUtils.random(minRange, maxRange), minRange, maxRange);
        
        float a = baseColor.a; float r = baseColor.r; float g = baseColor.g; float b = baseColor.b;
        
        
        r += MathUtils.randomBoolean() ? diff : -diff;
        g += MathUtils.randomBoolean() ? diff : -diff;
        b += MathUtils.randomBoolean() ? diff : -diff;
        
        if (trans)
            a = 0F;
        
        newColor = new Color(r, g, b, a);
        
        return newColor;
    }

    public static Color ColorDeviator(Color baseColor, float minRange, float maxRange, boolean trans) {
        return ColorDeviator(baseColor, minRange, maxRange, trans, false, false);
    }

    public static Color ColorDeviator(Color baseColor, float minRange, float maxRange) {
        return ColorDeviator(baseColor, minRange, maxRange, false, false, false);
    }
    
    private float getColorSaturation(Color color) {
        float sa;
        float R = color.r / 255F; float G = color.g / 255F; float B = color.b / 255F;
        float Cmax = Math.max(Math.max(R, G), B);
        float Cmin = Math.min(Math.min(R, G), B);
        float Diff = Math.abs(Cmax - Cmin);
        
        if (Cmax != 0)
            sa = Diff / Cmax;
        else sa = 0F;
        
        return sa;
    }
    
    private float getColorValue(Color color) {
        float va;
        float R = color.r / 255F; float G = color.g / 255F; float B = color.b / 255F;
        va = Math.max(Math.max(R, G), B);
        
        return va;
    }
}
