package TheManiac.monsters.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;

public class wanderingGhostBody implements Disposable {
    public static final String ID = "maniac:wanderingGhostBody";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private float rotSpeed;
    public float targetRotSpeed;
    private BobEffect effect;
    private AbstractMonster m;
    private static final String IMG_DIR = "maniacMod/images/monsters/enemies/wanderingGhost/ghostBody";
    private static final int W = 512;
    private Texture plasma1;
    private Texture plasma2;
    private Texture plasma3;
    private Texture shadow;
    private float plasma1Angle;
    private float plasma2Angle;
    private float plasma3Angle;
    private static final float BODY_OFFSET_Y = 256F * Settings.scale;
    
    public wanderingGhostBody(AbstractMonster m) {
        this.rotSpeed = 1F;
        this.targetRotSpeed = 30F;
        this.effect = new BobEffect(0.75F); 
        this.plasma1Angle = 0.0F;
        this.plasma2Angle = 0.0F;
        this.plasma3Angle = 0.0F;
        this.m = m;
        this.plasma1 = ImageMaster.loadImage(IMG_DIR + "/plasma1.png");
        this.plasma2 = ImageMaster.loadImage(IMG_DIR + "/plasma2.png");
        this.plasma3 = ImageMaster.loadImage(IMG_DIR + "/plasma3.png");
        this.shadow = ImageMaster.loadImage(IMG_DIR + "/shadow.png");
    }
    
    public void update() {
        this.effect.update();
        this.plasma1Angle += this.rotSpeed * Gdx.graphics.getDeltaTime();
        this.plasma2Angle -= this.rotSpeed / 2.0F * Gdx.graphics.getDeltaTime();
        this.plasma3Angle += this.rotSpeed / 3.0F * Gdx.graphics.getDeltaTime();
        
        this.rotSpeed = MathHelper.fadeLerpSnap(this.rotSpeed, this.targetRotSpeed);
        this.effect.speed = this.rotSpeed * Gdx.graphics.getDeltaTime();
    }
    
    public void render(SpriteBatch sb) {
        sb.setColor(this.m.tint.color);
        sb.draw(this.plasma3, this.m.drawX - 256.0F + this.m.animX + 12.0F * Settings.scale, this.m.drawY + this.m.animY + this.effect.y * 2.0F - 256.0F + BODY_OFFSET_Y, 
                256.0F, 256.0F, 512.0F, 512.0F, Settings.scale * 0.95F, Settings.scale * 0.95F, this.plasma3Angle, 0, 0, 512, 512, false, false);
        sb.draw(this.plasma2, this.m.drawX - 256.0F + this.m.animX + 6.0F * Settings.scale, this.m.drawY + this.m.animY + this.effect.y - 256.0F + BODY_OFFSET_Y, 
                256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, this.plasma2Angle, 0, 0, 512, 512, false, false);
        sb.draw(this.plasma1, this.m.drawX - 256.0F + this.m.animX, this.m.drawY + this.m.animY + this.effect.y * 0.5F - 256.0F + BODY_OFFSET_Y, 
                256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, this.plasma1Angle, 0, 0, 512, 512, false, false);
        sb.draw(this.shadow, this.m.drawX - 256.0F + this.m.animX + 12.0F * Settings.scale, this.m.drawY + this.m.animY + this.effect.y / 4.0F - 15.0F * Settings.scale - 256.0F + BODY_OFFSET_Y, 
                256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 512, false, false);
    }
    
    @Override
    public void dispose() {
        this.plasma1.dispose();
        this.plasma2.dispose();
        this.plasma3.dispose();
        this.shadow.dispose();
    }
}
