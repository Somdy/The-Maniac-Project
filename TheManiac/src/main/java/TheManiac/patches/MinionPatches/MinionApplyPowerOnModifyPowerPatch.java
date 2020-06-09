package TheManiac.patches.MinionPatches;

import TheManiac.character.TheManiacCharacter;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch( clz = AbstractDungeon.class, method = "onModifyPower")
public class MinionApplyPowerOnModifyPowerPatch {
    
    @SpireInsertPatch(rloc = 0)
    public static void MinionApplyPowerInsert() {
        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player instanceof TheManiacCharacter) {
                ((TheManiacCharacter) AbstractDungeon.player).applyMinionPowers();
            }
        }
    }
}
