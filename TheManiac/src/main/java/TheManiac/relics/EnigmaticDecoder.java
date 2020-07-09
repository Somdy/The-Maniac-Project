package TheManiac.relics;

import TheManiac.powers.LockedPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EnigmaticDecoder extends AbstractManiacRelic {
    public static final String ID = "maniac:EnigmaticDecoder";
    private static final String IMG_PATH = "maniacMod/images/relics/enigmaticDecoder.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/enigmaticDecoder_outline.png";

    public EnigmaticDecoder() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.SPECIAL, LandingSound.CLINK, true);
        this.canTarget = true;
    }

    @Override
    public void receivePostTargeted(AbstractCreature source, AbstractCreature target) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null && p.hasRelic(EnigmaticEncoder.ID)) {
            EnigmaticEncoder encoder = (EnigmaticEncoder) p.getRelic(EnigmaticEncoder.ID);
            if (encoder.canDecoderUse)
                this.addToBot(new ApplyPowerAction(target, source, new LockedPower(target, 1), 1));
            encoder.canDecoderUse = false;
        }
        
        /*if (canUse) {
            this.addToBot(new ApplyPowerAction(target, source, new LockedPower(target, 1), 1));
            canUse = false;
        }*/
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EnigmaticDecoder();
    }
}