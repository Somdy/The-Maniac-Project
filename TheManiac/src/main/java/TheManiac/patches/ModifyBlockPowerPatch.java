package TheManiac.patches;

import TheManiac.powers.AbstractManiacPower;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch( clz = AbstractCreature.class, method = "addBlock")
public class ModifyBlockPowerPatch {
    
    @SpireInsertPatch( locator = Locator.class, localvars = {"tmp"} )
    public static void PowerInsert(AbstractCreature _instance, int blockAmount, @ByRef float[] tmp) {
        if (_instance.powers.isEmpty()) {
            return;
        }

        for (AbstractPower power : _instance.powers) {
            if (power instanceof AbstractManiacPower) {
                tmp[0] = ((AbstractManiacPower) power).modifyBlockOnGaining(tmp[0]);
            }
        }
    }
    
    private static class Locator extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
        }
    }
}