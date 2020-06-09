package TheManiac.patches.MinionPatches;

import TheManiac.minions.AbstractManiacMinion;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterNewField {
    
    @SpirePatch( clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class AttackMinionField {
        public static SpireField<AbstractManiacMinion> targetMinion = new SpireField<>(() -> null);
    }
}
