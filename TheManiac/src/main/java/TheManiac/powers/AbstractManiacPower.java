package TheManiac.powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractManiacPower extends AbstractPower {
    private Logger logger = LogManager.getLogger(AbstractManiacPower.class.getName());
    public static TextureAtlas powerAtlas = new TextureAtlas(Gdx.files.internal("maniacMod/images/powers/ManiacPowers.atlas"));
    public boolean isUnravelling;
    
    public AbstractManiacPower() {
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;
        this.isPostActionPower = false;
        this.canGoNegative = false;
        this.isUnravelling = false;
    }

    protected void loadImg(String name) {
        this.region128 = powerAtlas.findRegion("lp/" + name);
        this.region48 = powerAtlas.findRegion("sp/" + name);
    }
    
    public float modifyBlockOnGaining(float blockAmount) {
        return blockAmount;
    }
}
