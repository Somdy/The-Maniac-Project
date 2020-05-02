package TheManiac.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DamagedAnvil extends CustomRelic {
    public static final String ID = "maniac:DamagedAnvil";
    private static final String IMG_PATH = "maniacMod/images/relics/damagedAnvil.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/damagedAnvil_outline.png";

    public DamagedAnvil() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.counter = 0;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (this.counter == 0) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new UpgradeRandomCardAction());
            this.grayscale = true;
            this.counter = -1;
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DamagedAnvil();
    }
}
