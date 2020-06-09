package TheManiac.relics;

import TheManiac.TheManiac;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ResurrectionStone extends CustomRelic {
    public static final String ID = TheManiac.makeID("ResurrectionStone");
    private static final String IMG_PATH = "maniacMod/images/relics/resurrectionStone.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/resurrectionStone_outline.png";
    private final AbstractPlayer player = AbstractDungeon.player;
    
    public ResurrectionStone() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ResurrectionStone();
    }
}
