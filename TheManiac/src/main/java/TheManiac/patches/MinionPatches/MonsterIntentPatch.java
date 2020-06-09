package TheManiac.patches.MinionPatches;

import TheManiac.character.TheManiacCharacter;
import TheManiac.helper.ManiacImageMaster;
import TheManiac.helper.MinionHelper;
import TheManiac.minions.AbstractManiacMinion;
import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MonsterIntentPatch {
    
    @SpirePatch( clz = AbstractMonster.class, method = "createIntent")
    public static class MonsterCreateIntentPach {
        
        @SpireInsertPatch(locator = IntentLocator.class)
        public static void InsertIntent(AbstractMonster _instance) {
            AbstractMonster.Intent r_intent = _instance.intent;
            if (AbstractDungeon.player instanceof TheManiacCharacter && ((TheManiacCharacter) AbstractDungeon.player).hasMinions()) {
                if ((r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION || r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_BUFF
                        || r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEFEND 
                        || r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEBUFF) && MinionHelper.getMonsterTarget(_instance) == null) {
                    AbstractManiacMinion target = (AbstractManiacMinion) ((TheManiacCharacter) AbstractDungeon.player).getMinions().getRandomMonster();
                    MinionHelper.setMonsterTarget(_instance, target);
                }
            }
        }
        
        private static class IntentLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "updateIntentTip");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    @SpirePatch( clz = AbstractMonster.class, method = "getIntentImg")
    public static class MonsterIntentImgPatch {
        
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Texture> GetIntentImgInsert(AbstractMonster _instance) {
            AbstractMonster.Intent r_intent = _instance.intent;
            if (r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION || r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_BUFF 
            || r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEFEND || r_intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEBUFF) {
                BaseMod.logger.info(_instance.name + "正在更新意图图像：" + _instance.intent);
                return SpireReturn.Return(getCustomAtkIntentImg(_instance));
            }
            
            return SpireReturn.Continue();
        }
        
        private static Texture getCustomAtkIntentImg(AbstractMonster _instance) {
            try {
                int tmp;
                Field isMultiDmg = AbstractMonster.class.getDeclaredField("isMultiDmg");
                isMultiDmg.setAccessible(true);
                Field intentDmg = AbstractMonster.class.getDeclaredField("intentDmg");
                intentDmg.setAccessible(true);
                Field intentMultiAmt = AbstractMonster.class.getDeclaredField("intentMultiAmt");
                intentMultiAmt.setAccessible(true);

                if (isMultiDmg.getBoolean(_instance)) {
                    tmp = intentDmg.getInt(_instance) * intentMultiAmt.getInt(_instance);
                } else {
                    tmp = intentDmg.getInt(_instance);
                }
                
                if (tmp < 5)
                    return ManiacImageMaster.INTENT_ATK_MINION_1;
                if (tmp < 10)
                    return ManiacImageMaster.INTENT_ATK_MINION_2;
                if (tmp < 15)
                    return ManiacImageMaster.INTENT_ATK_MINION_3;
                if (tmp < 20)
                    return ManiacImageMaster.INTENT_ATK_MINION_4;
                if (tmp < 25) {
                    return ManiacImageMaster.INTENT_ATK_MINION_5;
                }
                return (tmp < 30) ? ManiacImageMaster.INTENT_ATK_MINION_7 : ManiacImageMaster.INTENT_ATK_MINION_6;
                
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    
    @SpirePatch( clz = AbstractMonster.class, method = "updateIntentVFX")
    public static class UpdateIntentVfxPatch {
        
        public static SpireReturn UpdateIntentVfxPrefix(AbstractMonster _instance) {
            try {
                Field intentParticleTimer = AbstractMonster.class.getDeclaredField("intentParticleTimer");
                intentParticleTimer.setAccessible(true);
                Field intentVfx = AbstractMonster.class.getDeclaredField("intentVfx");
                intentVfx.setAccessible(true);

                if (_instance.intentAlpha > 0.0F) {
                    if (_instance.intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEFEND) {
                        intentParticleTimer.setFloat(_instance, intentParticleTimer.getFloat(_instance) - Gdx.graphics.getDeltaTime());
                        float valIntentParticleTime = intentParticleTimer.getFloat(_instance);
                        if (valIntentParticleTime < 0.0F) {
                            intentParticleTimer.setFloat(_instance, 0.5F);
                            ((ArrayList)intentVfx.get(_instance)).add(new ShieldParticleEffect(_instance.intentHb.cX, _instance.intentHb.cY));
                            return SpireReturn.Return(null);
                        }

                    } else if (_instance.intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_BUFF) {
                        intentParticleTimer.setFloat(_instance, intentParticleTimer.getFloat(_instance) - Gdx.graphics.getDeltaTime());
                        float valIntentParticleTime = intentParticleTimer.getFloat(_instance);
                        if (valIntentParticleTime < 0.0F) {
                            intentParticleTimer.setFloat(_instance, 0.1F);
                            ((ArrayList)intentVfx.get(_instance)).add(new BuffParticleEffect(_instance.intentHb.cX, _instance.intentHb.cY));
                            return SpireReturn.Return(null);
                        }

                    } else if (_instance.intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEBUFF) {
                        intentParticleTimer.setFloat(_instance, intentParticleTimer.getFloat(_instance) - Gdx.graphics.getDeltaTime());
                        float valIntentParticleTime = intentParticleTimer.getFloat(_instance);
                        if (valIntentParticleTime < 0.0F) {
                            intentParticleTimer.setFloat(_instance, 1.0F);
                            ((ArrayList)intentVfx.get(_instance)).add(new DebuffParticleEffect(_instance.intentHb.cX, _instance.intentHb.cY));
                            return SpireReturn.Return(null);
                        }
                    }
                }
                return SpireReturn.Continue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return SpireReturn.Continue();
        }
    }
    
    @SpirePatch( clz = AbstractMonster.class, method = "updateIntentTip")
    public static class UpdateIntentTipPatch {
        public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:MonsterIntentPatch");
        private static final String[] TEXT = uiStrings.TEXT;
        
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn UpdateIntentTipInsert(AbstractMonster _instance) {
            if (AbstractDungeon.player instanceof TheManiacCharacter) {
                try {
                    Field r_intentTip = AbstractMonster.class.getDeclaredField("intentTip");
                    r_intentTip.setAccessible(true);
                    Field r_isMultiDamage = AbstractMonster.class.getDeclaredField("isMultiDmg");
                    r_isMultiDamage.setAccessible(true);
                    Field r_intentDmg = AbstractMonster.class.getDeclaredField("intentDmg");
                    r_intentDmg.setAccessible(true);
                    Field r_intentMultiAmt = AbstractMonster.class.getDeclaredField("intentMultiAmt");
                    r_intentMultiAmt.setAccessible(true);
                    Method getAttackIntentTip = AbstractMonster.class.getDeclaredMethod("getAttackIntentTip");
                    getAttackIntentTip.setAccessible(true);

                    PowerTip intentTip = (PowerTip)r_intentTip.get(_instance);
                    boolean isMultiDamage = r_isMultiDamage.getBoolean(_instance);
                    int intentDmg = r_intentDmg.getInt(_instance);
                    int intentMultiAmt = r_intentMultiAmt.getInt(_instance);

                    AbstractMonster.Intent intent = _instance.intent;
                    AbstractManiacMinion target = MinionHelper.getMonsterTarget(_instance);
                    
                    if (target != null) {
                        if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION) {
                            intentTip.header = TEXT[0];
                            if (isMultiDamage) {
                                intentTip.body = TEXT[1] + target.name + TEXT[2] + intentDmg + TEXT[3] + intentMultiAmt + TEXT[5];
                            } else {
                                intentTip.body = TEXT[1] + target.name + TEXT[2] + intentDmg + TEXT[4];
                            }
                            intentTip.img = (Texture) getAttackIntentTip.invoke(_instance, new Object[0]);
                            BaseMod.logger.info(_instance.name + "成功更新意图：" + intent);
                        }
                        else if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_BUFF) {
                            intentTip.header = TEXT[0];
                            if (isMultiDamage) {
                                intentTip.body = TEXT[6] + target.name + TEXT[2] + intentDmg + TEXT[3] + intentMultiAmt + TEXT[5];
                            } else {
                                intentTip.body = TEXT[6] + target.name + TEXT[2] + intentDmg + TEXT[4];
                            }
                            intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                            BaseMod.logger.info(_instance.name + "成功更新意图：" + intent);
                        }
                        else if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEFEND) {
                            intentTip.header = TEXT[0];
                            if (isMultiDamage) {
                                intentTip.body = TEXT[9] + target.name + TEXT[2] + intentDmg + TEXT[3] + intentMultiAmt + TEXT[5];
                            } else {
                                intentTip.body = TEXT[9] + target.name + TEXT[2] + intentDmg + TEXT[4];
                            }
                            intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
                            BaseMod.logger.info(_instance.name + "成功更新意图：" + intent);
                        }
                        else if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEBUFF) {
                            intentTip.header = TEXT[7];
                            if (isMultiDamage) {
                                intentTip.body = TEXT[8] + target.name + TEXT[2] + intentDmg + TEXT[3] + intentMultiAmt + TEXT[5];
                            } else {
                                intentTip.body = TEXT[8] + target.name + TEXT[2] + intentDmg + TEXT[4];
                            }
                            intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
                            BaseMod.logger.info(_instance.name + "成功更新意图：" + intent);
                        } else {
                            return SpireReturn.Continue();
                        }
                    } else {
                        if (intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION || intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_BUFF
                                || intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEBUFF 
                                || intent == MonsterIntentsOnMinionPatch.Enums.ATTACK_MINION_DEFEND) {
                            MinionHelper.returnMonsterTarget(_instance);
                            return SpireReturn.Return(null);
                        }
                        
                        return SpireReturn.Continue();
                    }
                    
                    r_intentTip.set(_instance, intentTip);
                    return SpireReturn.Return(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            return SpireReturn.Continue();
        }
    }
    
}
