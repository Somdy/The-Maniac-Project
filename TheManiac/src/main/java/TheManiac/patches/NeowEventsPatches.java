package TheManiac.patches;

import TheManiac.TheManiac;
import TheManiac.neow.ObtainManuscripts;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class NeowEventsPatches {

    /*
    @SpirePatch( clz = NeowEvent.class, method = "buttonEffect" )
    public static class ObtainScriptsOption {
        @SpireInsertPatch(rloc = 0)
        public static void Insert(NeowEvent _instance, int buttonPressed) throws Exception {
            Field bossCount = _instance.getClass().getDeclaredField("bossCount");
            bossCount.setAccessible(true);

            if ((Integer) bossCount.get(_instance) < 1) {
                bossCount.set(_instance, 1);
            }
        }
    }
    
     */
    
    
    @SpirePatch( clz = NeowEvent.class, method = "blessing" )
    public static class ManuscriptsBlessingPatch {
        @SpireInsertPatch(rloc = 12, localvars = {"rewards"})
        public static void Insert(NeowEvent _instance, ArrayList<NeowReward> rewards) { 
            rewards.add(rewards.size(), new ObtainManuscripts());
        }
    }

    @SpirePatch( clz = NeowEvent.class, method = "blessing" )
    public static class ManuscriptsOptionPatch {
        
        @SpireInsertPatch(rloc = 18, localvars = {"rewards"})
        public static void Insert(NeowEvent _instance, ArrayList<NeowReward> rewards) {
            _instance.roomEventText.addDialogOption(rewards.get(rewards.size() - 1).optionLabel);
        }
    }
    
    @SpirePatch( clz = NeowEvent.class, method = "buttonEffect" )
    public static class ObtainScriptsOptionPatch {
        public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:NeowReward_Manuscripts");
        
        @SpireInsertPatch(rloc = 63, localvars = {"rewards"})
        public static void Insert(NeowEvent _instance, int buttonPressed, ArrayList<NeowReward> rewards) throws Exception {
            if (buttonPressed < 4) {
                return;
            }
            rewards.get(buttonPressed).activate();
            Method method = NeowEvent.class.getDeclaredMethod("talk", String.class);
            method.setAccessible(true);
            int index = MathUtils.random(1, 2);
            method.invoke(_instance, uiStrings.TEXT[index]);
        }
    }
    
    /*
    @SpireEnum
    public static NeowReward.NeowRewardType Manuscripts;
    
    @SpirePatch( clz = NeowReward.class, method = "getRewardOptions" )
    public static class ObtainManuscriptsOption {
        public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:NeowReward_Manuscripts");
        private static final String TEXT = uiStrings.TEXT[1];
        
        public static ArrayList<NeowReward.NeowRewardDef> Postfix(ArrayList<NeowReward.NeowRewardDef> _result, NeowReward _instance, int category) {
            _result.add(new NeowReward.NeowRewardDef(Manuscripts, TEXT));
            return _result;
        }
    }
    
    @SpirePatch( clz = NeowReward.class, method = "activate" )
    public static class ActivateRewardPatch {
        public static void Prefix(NeowReward _instance) {
            if (_instance.type == Manuscripts) {
                ObtainManuscriptsAccess.ObtainManuscripts();
            }
        }
    }
    */
}
