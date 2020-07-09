package TheManiac.patches;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class ExhaustOtherCardPatch {
    
    @SpirePatch( clz = CardGroup.class, method = "moveToExhaustPile" )
    public static class onMoveToExhaustPile {
        
        @SpireInsertPatch(rloc = 0)
        public static void Insert(CardGroup _instance, AbstractCard _card) {
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onExhaustOtherCard(_card, true);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onExhaustOtherCard(_card, false);
                }
            }

            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onExhaustOtherCard(_card, false);
                }
            }
        }

        @SpirePostfixPatch
        public static void ShroudPostfix(CardGroup _instance, AbstractCard card) {
            if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).isShroud) {
                ((AbstractManiacCard) card).returnShroudCard();
            }
        }
        
        @SpireInsertPatch(locator = ShroudLocator.class)
        public static SpireReturn ShroudedSkipperInsert(CardGroup _instance, AbstractCard card) {
            if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).shrouded) {
                AbstractDungeon.player.onCardDrawOrDiscard();
                return SpireReturn.Return(null);
            }
            
            return SpireReturn.Continue();
        }
        
        private static class ShroudLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "addToTop");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
}
