package TheManiac.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MakerPen extends CustomRelic {
    public static final String ID = "maniac:MarkerPen";
    private static final String IMG_PATH = "maniacMod/images/relics/makerPen.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/makerPen_outline.png";

    public MakerPen() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MakerPen();
    }
}
