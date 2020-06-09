package TheManiac.patches.MinionPatches;

import TheManiac.character.TheManiacCharacter;
import TheManiac.helper.MinionHelper;
import TheManiac.minions.AbstractManiacMinion;
import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.lang.reflect.Field;

public class MonsterIntentsOnMinionPatch {
    
    public static class Enums {
        @SpireEnum
        public static AbstractMonster.Intent ATTACK_MINION;
        @SpireEnum
        public static AbstractMonster.Intent ATTACK_MINION_BUFF;
        @SpireEnum
        public static AbstractMonster.Intent ATTACK_MINION_DEBUFF;
        @SpireEnum
        public static AbstractMonster.Intent ATTACK_MINION_DEFEND;
    }
    
    @SpirePatch( clz = AbstractMonster.class, method = "setMove", paramtypez = {
            String.class,
            byte.class,
            AbstractMonster.Intent.class,
            int.class,
            int.class,
            boolean.class
    })
    public static class MosterChangeMovePatch {
        
        @SpirePostfixPatch
        public static void MonsterMovePostfix(AbstractMonster _instance, String moveName, byte nextMove, AbstractMonster.Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
            if (AbstractDungeon.player != null) {
                if (AbstractDungeon.player instanceof TheManiacCharacter && ((TheManiacCharacter) AbstractDungeon.player).hasMinions()) {
                    AbstractMonster.EnemyType type = ((TheManiacCharacter) AbstractDungeon.player).getMinion().type;
                    switch (intent) {
                        case ATTACK:
                            MayAttackMinion(_instance, type, nextMove, Enums.ATTACK_MINION, baseDamage, multiplier, isMultiDamage);
                            break;
                        case ATTACK_BUFF:
                            MayAttackMinion(_instance, type, nextMove, Enums.ATTACK_MINION_BUFF, baseDamage, multiplier, isMultiDamage);
                            break;
                        case ATTACK_DEBUFF:
                            MayAttackMinion(_instance, type, nextMove, Enums.ATTACK_MINION_DEBUFF, baseDamage, multiplier, isMultiDamage);
                            break;
                        case ATTACK_DEFEND:
                            MayAttackMinion(_instance, type, nextMove, Enums.ATTACK_MINION_DEFEND, baseDamage, multiplier, isMultiDamage);
                            break;
                    }
                }
            }
        }
        
        private static void MayAttackMinion(AbstractMonster _instance, AbstractMonster.EnemyType type, byte nextMove, AbstractMonster.Intent newIntent, int baseDmg, int multiplier, boolean isMultiDamage) {
            try {
                BaseMod.logger.info(_instance.name + "有可能改变意图...");
                
                float chance;
                
                if (type == AbstractManiacMinion.Enums.MINION_DEFENSIVE) {
                    chance = 0.55F;
                }
                else if (type == AbstractManiacMinion.Enums.MINION_AGGRESSIVE) {
                    chance = 0.35F;
                }
                else if (type == AbstractManiacMinion.Enums.MINION_SUPPORTIVE) {
                    chance = 0.14F;
                } else {
                    chance = 0F;
                    BaseMod.logger.info("ERROR：未知的随从类型：" + type);
                }
                
                if (AbstractDungeon.aiRng.randomBoolean(chance)) {
                    BaseMod.logger.info(_instance.name + "正在改变意图...");
                    Field move = AbstractMonster.class.getDeclaredField("move");
                    move.setAccessible(true);

                    EnemyMoveInfo info = new EnemyMoveInfo(nextMove, newIntent, baseDmg, multiplier, isMultiDamage);
                    move.set(_instance, info);
                } else {
                    MinionHelper.setMonsterTarget(_instance, null);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
