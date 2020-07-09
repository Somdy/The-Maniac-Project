package TheManiac.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EnigmaticEncoder extends AbstractManiacRelic {
    public static final String ID = "maniac:EnigmaticEncoder";
    private static final String IMG_PATH = "maniacMod/images/relics/enigmaticEncoder.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/enigmaticEncoder_outline.png";
    private int uses;
    public boolean canDecoderUse;
    
    public EnigmaticEncoder() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.SPECIAL, LandingSound.CLINK, true);
        this.counter = 2;
        this.uses = 0;
        this.canDecoderUse = true;
        getUpdatedDescription();
    }

    @Override
    public void atTurnStart() {
        this.canUse = true;
        if (!canDecoderUse) canDecoderUse = true;
    }

    @Override
    public void onRightClick() {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.counter));
        if (this.uses < 3) this.counter += 2;
        this.uses++;
        this.canUse = false;
        getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        this.description = DESCRIPTIONS[0] + this.counter + DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        return this.description;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EnigmaticEncoder();
    }

    @Override
    public void receivePostTargeted(AbstractCreature source, AbstractCreature target) {
        
    }
}
