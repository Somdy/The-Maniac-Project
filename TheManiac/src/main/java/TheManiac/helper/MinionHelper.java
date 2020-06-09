package TheManiac.helper;

import TheManiac.character.TheManiacCharacter;
import TheManiac.minions.AbstractManiacMinion;
import TheManiac.patches.MinionPatches.MonsterIntentsOnMinionPatch;
import TheManiac.patches.MinionPatches.MonsterNewField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MinionHelper {
    
    public static boolean summonMinion(AbstractManiacMinion minion, AbstractPlayer player) {
        if (player instanceof TheManiacCharacter) {
            return ((TheManiacCharacter) player).summonMinion(minion);
        }
        return false;
    }
    
    public static void setMonsterTarget(AbstractMonster monster, AbstractManiacMinion target) {
        MonsterNewField.AttackMinionField.targetMinion.set(monster, target);
    }
    
    public static AbstractManiacMinion getMonsterTarget(AbstractMonster monster) {
        return MonsterNewField.AttackMinionField.targetMinion.get(monster);
    }
    
    public static void swtichMonsterTarget(AbstractMonster monster, AbstractManiacMinion newTarget) {
        if (newTarget == null) {
            returnMonsterTarget(monster);
            return;
        }
        setMonsterTarget(monster, newTarget);
        monster.applyPowers();
    }
    
    public static void returnMonsterTarget(AbstractMonster monster) {
        AbstractMonster.Intent intent = monster.intent;
        
        if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION) {
            monster.intent = AbstractMonster.Intent.ATTACK;
        }
        else if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_BUFF) {
            monster.intent = AbstractMonster.Intent.ATTACK_BUFF;
        }
        else if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEBUFF) {
            monster.intent = AbstractMonster.Intent.ATTACK_DEBUFF;
        }
        else if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEFEND) {
            monster.intent = AbstractMonster.Intent.ATTACK_DEFEND;
        }
    }
}
