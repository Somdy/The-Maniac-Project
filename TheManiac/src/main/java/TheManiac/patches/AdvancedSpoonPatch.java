package TheManiac.patches;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class AdvancedSpoonPatch {
    
    @SpirePatch( clz = UseCardAction.class, method = "update")
    public static class ShroudMustExhaust {
        
        @SpireInsertPatch( rloc = 48, localvars = {"spoonProc"})
        public static void ShroudInsert(UseCardAction _instance, @ByRef boolean[] spoonProc) throws Exception {
            Field targetCard = UseCardAction.class.getDeclaredField("targetCard");
            targetCard.setAccessible(true);
            Type type = targetCard.getGenericType();
            if (_instance.exhaustCard && type instanceof AbstractManiacCard && ((AbstractManiacCard) type).isShroud) {
                spoonProc[0] = false;
            }
        }
    }
}