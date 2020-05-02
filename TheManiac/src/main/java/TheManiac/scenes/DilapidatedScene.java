package TheManiac.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.DeathScreenFloatyEffect;

import java.util.ArrayList;

public class DilapidatedScene extends AbstractScene {
    private ArrayList<DeathScreenFloatyEffect> particles;
    private Color color;
    private Color[] colors;
    private boolean addColor;
    
    public DilapidatedScene() {
        super("maniacMod/images/dilapidatedScene/scene.atlas");
        this.particles = new ArrayList<>();
        this.ambianceName = "AMBIANCE_BEYOND";
        fadeInAmbiance();
        this.color = Color.DARK_GRAY.cpy();
        this.colors = new Color[] {this.color, Color.SALMON, Color.BROWN, Color.CORAL, Color.VIOLET};
        this.color.a = 0F;
        this.addColor = true;
    }

    @Override
    public void update() {
        if (this.particles.size() < 60) {
            this.particles.add(new DeathScreenFloatyEffect());
        }
        
        for (int i = this.particles.size() - 1; i >= 0; i--) {
            DeathScreenFloatyEffect dfe = this.particles.get(i);
            dfe.update();
            if (dfe.isDone) {
                this.particles.remove(i);
            }
        }
        
        if (addColor) {
            this.color.a += Gdx.graphics.getDeltaTime() / 8F;
            if (this.color.a >= 0.3F) {
                this.addColor = false;
                this.color.a = 0.3F;
            }
        } else {
            this.color.a -= Gdx.graphics.getDeltaTime() / 8.0F;
            if (this.color.a <= 0.0F) {
                this.addColor = true;
                this.color = this.colors[MathUtils.random(this.colors.length - 1)].cpy();
                this.color.a = 0.0F;
            }
        }
    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setColor(this.color);
        for (AbstractGameEffect effect : this.particles) {
            if (effect.renderBehind) {
                effect.render(sb);
            }
        }
    }

    @Override
    public void renderCombatRoomFg(SpriteBatch sb) {
        for (AbstractGameEffect effect : this.particles) {
            if (!effect.renderBehind) {
                effect.render(sb);
            }
        }
    }

    @Override
    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderAtlasRegionIf(sb, this.campfireBg, true);
        for (DeathScreenFloatyEffect dfe : this.particles) {
            dfe.render(sb);
        }
    }

    @Override
    public void randomizeScene() {

    }
}
