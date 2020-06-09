package TheManiac.patches.MinionPatches;

import TheManiac.helper.MinionHelper;
import TheManiac.minions.AbstractManiacMinion;
import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MonsterCaldmgMinionPatch {
    
    @SpirePatch( clz = AbstractMonster.class, method = "calculateDamage")
    public static class MonsterCalculateDmgOnMinion {
        
        @SpirePrefixPatch
        public static SpireReturn MonsterFixCaldmgPrefix(AbstractMonster _instance, int dmg) {
            try {
                Field r_intentDmg = AbstractMonster.class.getDeclaredField("intentDmg");
                r_intentDmg.setAccessible(true);

                AbstractMonster.Intent intent = _instance.intent;
                if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION || intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_BUFF
                        || intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEBUFF 
                        || intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEFEND) {
                    AbstractManiacMinion target = MinionHelper.getMonsterTarget(_instance);
                    
                    if (target == null) {
                        return SpireReturn.Continue();
                    }

                    BaseMod.logger.info(_instance.name + " 正在对 " + target.name + " 计算伤害.");
                    
                    float tmp = dmg;

                    //BaseMod.logger.info(_instance.name + " 的初始伤害为：" + tmp);
                    
                    for (AbstractPower p : _instance.powers) {
                        tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
                    }
                    for (AbstractPower p : target.powers) {
                        tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
                    }
                    for (AbstractPower p : _instance.powers) {
                        tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
                    }
                    for (AbstractPower p : target.powers) {
                        tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL);
                    }

                    dmg = MathUtils.floor(tmp);
                    if (dmg < 0) {
                        dmg = 0;
                    }

                    BaseMod.logger.info(_instance.name + " 对 " + target.name + " 计算后的伤害为：" + dmg);
                    
                    r_intentDmg.set(_instance, dmg);
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            } catch (Exception e) {
                e.printStackTrace();
                return SpireReturn.Continue();
            }
        }
    }
    
    @SpirePatch( clz = AbstractMonster.class, method = "applyPowers")
    public static class MonsterApplyPowersOnMinion {
        
        @SpirePrefixPatch
        public static SpireReturn MonsterFixApplyPowersPrefix(AbstractMonster _instance) {
            try {
                Field r_move = AbstractMonster.class.getDeclaredField("move");
                r_move.setAccessible(true);
                Field r_intentImg = AbstractMonster.class.getDeclaredField("intentImg");
                r_intentImg.setAccessible(true);
                Method r_calDmg = AbstractMonster.class.getDeclaredMethod("calculateDamage", int.class);
                r_calDmg.setAccessible(true);
                Method r_getIntentImg = AbstractMonster.class.getDeclaredMethod("getIntentImg");
                r_getIntentImg.setAccessible(true);
                Method r_updateIntentTip = AbstractMonster.class.getDeclaredMethod("updateIntentTip");
                r_updateIntentTip.setAccessible(true);

                EnemyMoveInfo move = (EnemyMoveInfo) r_move.get(_instance);
                
                AbstractManiacMinion target = MinionHelper.getMonsterTarget(_instance);
                
                if (target != null) {
                    BaseMod.logger.info(_instance.name + " 正在对 " + target.name + " 更新伤害.");
                    for (DamageInfo damage : _instance.damage) {
                        damage.applyPowers(_instance, target);
                        BaseMod.logger.info(_instance.name + " 对 " + target.name + " 的更新后伤害为：" + damage.base);
                    }
                    if (move.baseDamage > -1) {
                        r_calDmg.invoke(_instance, move.baseDamage);
                    }
                    
                    r_intentImg.set(_instance, r_getIntentImg.invoke(_instance));
                    r_updateIntentTip.invoke(_instance);
                    return SpireReturn.Return(null);
                } else {
                    return SpireReturn.Continue();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                return SpireReturn.Continue();
            }
        }
    }
}
