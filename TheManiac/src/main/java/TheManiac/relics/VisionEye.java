package TheManiac.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class VisionEye extends CustomRelic {
    public static final String ID = "maniac:VisionEye";
    private static final String IMG_PATH = "maniacMod/images/relics/visionEye.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/visionEye_outline.png";

    public VisionEye() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VisionEye();
    }
}
