package TheManiac.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class EnchantedAnvil extends CustomRelic {
    public static final String ID = "maniac:EnchantedAnvil";
    private static final String IMG_PATH = "maniacMod/images/relics/enchantedAnvil.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/enchantedAnvil_outline.png";
    private AbstractCard theCard = null;

    public EnchantedAnvil() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.BOSS, LandingSound.SOLID);
    }

    @Override
    public void onVictory() {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                tmpGroup.addToTop(c);
            }
        }
        if (!tmpGroup.isEmpty()) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.theCard = tmpGroup.getRandomCard(AbstractDungeon.miscRng);
            this.theCard.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(this.theCard);
        }
        if (this.theCard != null) {
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(this.theCard.makeStatEquivalentCopy()));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(Settings.ACTION_DUR_MED));
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic("maniac:DamagedAnvil");
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(DamagedAnvil.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i ++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(DamagedAnvil.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        }
        else {
            super.obtain();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EnchantedAnvil();
    }
}
