package TheManiac.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MakerPen extends AbstractManiacRelic {
    public static final String ID = "maniac:MarkerPen";
    private static final String IMG_PATH = "maniacMod/images/relics/makerPen.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/makerPen_outline.png";

    public MakerPen() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.UNCOMMON, LandingSound.CLINK, true);
        this.canTarget = true;
    }

    @Override
    public void onRightClick(AbstractCreature source, AbstractCreature target) {
        this.addToBot(new DamageAction(target, new DamageInfo(source, 10, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
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
