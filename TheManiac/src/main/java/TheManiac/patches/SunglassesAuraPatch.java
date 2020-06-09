package TheManiac.patches;

import TheManiac.relics.PossessedManuscripts;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

public class SunglassesAuraPatch {
    
    //Your actions may be not what you think
    @SpirePatch( clz = AbstractCard.class, method = "calculateCardDamage" )
    public static class ModifyCardCalculateDamageAction {
        
        @SpireInsertPatch(rloc = 18, localvars = {"tmp"})
        public static void InsertDamage(AbstractCard _instance, AbstractMonster mo, @ByRef float[] tmp) {
            if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(PossessedManuscripts.ID);
                if (relic instanceof PossessedManuscripts && ((PossessedManuscripts) relic).activeEffects.get(1)) {
                    if (AbstractDungeon.miscRng.randomBoolean(((PossessedManuscripts) relic).activeAmounts.get(1).floatValue())) {
                        tmp[0] *= MathUtils.random(0.45F, 0.95F);
                    }
                }
            }
        }
        
        @SpireInsertPatch(rloc = 76, localvars = {"tmp"})
        public static void InsertMultiDamage(AbstractCard _instance, AbstractMonster mo, float[] tmp) {
            if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(PossessedManuscripts.ID);
                if (relic instanceof PossessedManuscripts && ((PossessedManuscripts) relic).activeEffects.get(1)) {
                    for (int i = 0; i < tmp.length; i++) {
                        if (AbstractDungeon.miscRng.randomBoolean(((PossessedManuscripts) relic).activeAmounts.get(1).floatValue())) {
                            tmp[i] *= MathUtils.random(0.25F, 0.85F);
                        }
                    }
                }
            }
        }
    }
    
    @SpirePatch( clz = AbstractCreature.class, method = "addBlock" )
    public static class ModifyGainBlockAction {
        
        @SpireInsertPatch(locator = MathLocator.class, localvars = {"tmp"})
        public static void InsertBlock(AbstractCreature _instance, int blockAmount, @ByRef float[] tmp) {
            if (_instance.isPlayer && AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(PossessedManuscripts.ID);
                if (relic instanceof PossessedManuscripts && ((PossessedManuscripts) relic).activeEffects.get(1)) {
                    if (AbstractDungeon.miscRng.randomBoolean(((PossessedManuscripts) relic).activeAmounts.get(1).floatValue())) {
                        relic.flash();
                        tmp[0] *= MathUtils.random(0.5F, 1.5F);
                    }
                }
            }
        }
        
        private static class MathLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
    
    @SpirePatch( clz = AbstractPlayer.class, method = "useCard" )
    public static class ModifyCardTarget {
        
        @SpireInsertPatch(locator = TargetLocator.class)
        public static void InsertTarget(AbstractPlayer _instance, AbstractCard c, @ByRef AbstractMonster[] monster, int energyOnUse) {
            if (_instance.isPlayer && AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(PossessedManuscripts.ID);
                if (relic instanceof PossessedManuscripts && ((PossessedManuscripts) relic).activeEffects.get(1)) {
                    AbstractMonster mo = AbstractDungeon.getMonsters().getRandomMonster(monster[0], true);
                    if (mo != null) {
                        if (AbstractDungeon.miscRng.randomBoolean(((PossessedManuscripts) relic).activeAmounts.get(1).floatValue())) {
                            relic.flash();
                            monster[0] = mo;
                        }
                    }
                }
            }
        }
        
        private static class TargetLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "use");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
    
   //You may misread enemies' intents
    @SpirePatch( clz = AbstractMonster.class, method = "createIntent")
    public static class ModifyEnemyIntents {
        
        @SpireInsertPatch(rloc = 16)
        public static SpireReturn InsertIntents(AbstractMonster _instance) {
            if (_instance.intent != AbstractMonster.Intent.UNKNOWN) {
                if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
                    AbstractRelic relic = AbstractDungeon.player.getRelic(PossessedManuscripts.ID);
                    if (relic instanceof PossessedManuscripts && ((PossessedManuscripts) relic).activeEffects.get(1)) {
                        if (AbstractDungeon.miscRng.randomBoolean(((PossessedManuscripts) relic).activeAmounts.get(1).floatValue())) {
                            _instance.intent = AbstractMonster.Intent.UNKNOWN;
                        }
                    }
                }
            }
            return SpireReturn.Continue();
        }
   }
}
