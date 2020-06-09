package TheManiac.patches.Specials;

import TheManiac.helper.TargetIndicator;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

/*
public class TargetIndicatorPatch {
    
    @SpirePatch( clz = AbstractDungeon.class, method = "render")
    public static class InsertRender {
        
        @SpireInsertPatch(locator = RenderLocator.class)
        public static void InsertR(Object _obj, SpriteBatch sb) {
            TargetIndicator.render(sb);
        }
        
        private static class RenderLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");
                
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class InsertPostUpdate {
        @SpireInsertPatch(locator = UpdateLocator.class)
        public static void Insert(Object __obj) {
            TargetIndicator.update();
        }

        private static class UpdateLocator extends SpireInsertLocator {
            
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "update");

                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
}

 */
