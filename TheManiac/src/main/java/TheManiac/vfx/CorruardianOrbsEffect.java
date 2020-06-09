package TheManiac.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CorruardianOrbsEffect extends AbstractGameEffect {
    private static final Logger logger = LogManager.getLogger(CorruardianOrbsEffect.class.getName());
    protected TextureAtlas atlas;
    protected Skeleton skeleton;
    public com.esotericsoftware.spine.AnimationState state;
    protected AnimationStateData stateData;
    public static SkeletonMeshRenderer sr;
    private static final String ORBS_ATLAS = "maniacMod/images/monsters/enemies/CorruardianOrbs/skeleton.atlas";
    private static final String ORBS_JSON = "maniacMod/images/monsters/enemies/CorruardianOrbs/skeleton.json";
    private float x;
    private float y;
    
    public CorruardianOrbsEffect(float x, float y) {
        this.duration = MathUtils.random(0.8F, 1.2F);
        this.startingDuration = this.duration;
        this.renderBehind = false;
        this.x = x;
        this.y = y;
        this.color = new Color(Color.PURPLE);
        sr = new SkeletonMeshRenderer();
        sr.setPremultipliedAlpha(true);

        loadAnimation(ORBS_ATLAS, ORBS_JSON, 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.state.setTimeScale(0.8F);
    }

    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.scale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
    }

    public void attackEffect() {
        this.state.setAnimation(0, "Attack", false);
        this.state.setTimeScale(0.8F);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }
    
    public void hide() {
        this.isDone = true;
    }

    @Override
    public void update() {
        if (this.duration > 1.0F) { 
            this.duration -= Gdx.graphics.getDeltaTime();
        } else {
            this.duration += Gdx.graphics.getDeltaTime();
        }
        this.color.a = Interpolation.fade.apply(0.0F, 0.8F, this.duration / this.startingDuration);
    }

    @Override
    public void render(SpriteBatch sb) {
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setPosition(this.x, this.y);
        this.skeleton.setColor(new Color(1.0F, 1.0F, 1.0F, this.duration));
        this.skeleton.setFlip(false, false);
        sb.end();
        CardCrawlGame.psb.begin();
        sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();
        sb.begin();
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
        this.isDone = true;
    }
}
