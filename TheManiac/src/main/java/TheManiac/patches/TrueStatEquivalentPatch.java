package TheManiac.patches;

import TheManiac.cards.the_possessed.uncertainties.ShadowVisions;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch( clz = AbstractCard.class, method = "makeStatEquivalentCopy" )
public class TrueStatEquivalentPatch {
    
    @SpireInsertPatch( rloc = 21, localvars = {"card"})
    public static void Insert(AbstractCard _instance, AbstractCard card) {
        card.purgeOnUse = _instance.purgeOnUse;
        card.isEthereal = _instance.isEthereal;
        card.exhaust = _instance.exhaust;
        card.glowColor = _instance.glowColor;
        CardMarkFieldPatch.TranslocationCopyField.isGatewayCopy.set(card, CardMarkFieldPatch.TranslocationCopyField.isGatewayCopy.get(_instance));
        CardMarkFieldPatch.TranslocationCopyField.isGatewayNega.set(card, CardMarkFieldPatch.TranslocationCopyField.isGatewayNega.get(_instance));
        CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.set(card, CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.get(_instance));
    }
    
    private static boolean hasShadowVisions() {
        if (AbstractDungeon.player == null) {
            return false;
        }
        else if (AbstractDungeon.player.masterDeck.isEmpty()) {
            return false;
        }

        return AbstractDungeon.player.masterDeck.findCardById(ShadowVisions.ID) != null;
    }
    
}
