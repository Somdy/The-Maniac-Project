package TheManiac.relics;

import TheManiac.helper.TargetIndicator;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class AbstractManiacRelic extends CustomRelic implements TargetIndicator.TargetIndicatorSubscriber {
    public static final Logger logger = LogManager.getLogger(AbstractManiacRelic.class.getName());

    public boolean Clickable;
    private boolean RclickStart;
    private boolean Rclick;
    public boolean canTarget;
    public boolean certainTarget;
    public boolean canUse;
    public ArrayList<AbstractCreature> allows;

    public AbstractManiacRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx, boolean clickable) {
        super(id, texture, outline, tier, sfx);
        TargetIndicator.register(this);
        this.Clickable = clickable;
        this.RclickStart = false;
        this.Rclick = false;
        this.canTarget = false;
        this.certainTarget = false;
        this.canUse = true;
        this.allows = new ArrayList<>();
    }

    public void onRightClick() {
    }

    @Override
    public void update() {
        super.update();
        if (this.RclickStart && InputHelper.justReleasedClickRight) {
            if (this.hb.hovered) {
                this.Rclick = true;
            }
            this.RclickStart = false;
        }
        if (this.isObtained && this.hb != null && this.hb.hovered && InputHelper.justClickedRight) {
            this.RclickStart = true;
        }
        if (this.Rclick) {
            this.Rclick = false;
            if (this.Clickable && this.canUse) {
                if (canTarget) {
                    if (certainTarget) {
                        setAllows();
                        if (!allows.isEmpty()) TargetIndicator.active(this, new Vector2(this.currentX, this.currentY), allows);
                    }
                    else TargetIndicator.active(this, new Vector2(this.currentX, this.currentY));
                }
                onRightClick();
            }
        }
    }

    public void setAllows() {
    }
}