package TheManiac.relics;

import TheManiac.TheManiac;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SmartDetector extends CustomRelic {
    public static final String ID = TheManiac.makeID("SmartDetector");
    private static final String IMG_PATH = "maniacMod/images/relics/smartDetector.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/smartDetector_outline.png";
    
    public SmartDetector() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SmartDetector();
    }
}
