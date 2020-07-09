package TheManiac.patches;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class TriggerCardEffectPatch {
    
    @SpirePatch( clz = AbstractPlayer.class, method = "useCard")
    public static class triggerOnCardPlayed {
        
        @SpireInsertPatch( locator = Locator.class )
        public static void InsertTrigger(AbstractPlayer _instance, AbstractCard card, AbstractMonster monster, int energyOnUse) {
            if (!card.dontTriggerOnUseCard) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) c).triggerOnCardPlayed(card, monster, true, false);
                    }
                }

                for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                    if (c instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) c).triggerOnCardPlayed(card, monster, false, true);
                    }
                }

                for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    if (c instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) c).triggerOnCardPlayed(card, monster, false, true);
                    }
                }
            }
        }
        
        private static class Locator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "removeCard");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
    
    @SpirePatch( clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
    public static class triggerOnCardDrawn {
        
        @SpireInsertPatch( rloc = 14, localvars = {"c"})
        public static void DrawCardTrigger(AbstractPlayer _instance, int numCards, AbstractCard c) {
            if (AbstractDungeon.player != null) {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card instanceof AbstractManiacCard) {
                        ((AbstractManiacCard) card).onOtherCardDrawn(c, true, false);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (card instanceof AbstractManiacCard) {
                        ((AbstractManiacCard) card).onOtherCardDrawn(c, false, false);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (card instanceof AbstractManiacCard) {
                        ((AbstractManiacCard) card).onOtherCardDrawn(c, false, false);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
                    if (card instanceof AbstractManiacCard) {
                        ((AbstractManiacCard) card).onOtherCardDrawn(c, false, true);
                    }
                }
            }
        }
    }
}
