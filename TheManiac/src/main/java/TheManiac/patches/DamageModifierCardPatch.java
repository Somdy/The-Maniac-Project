package TheManiac.patches;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DamageModifierCardPatch {
    
    @SpirePatch( clz = AbstractPlayer.class, method = "damage" )
    public static class onAttackPlayer {
        
        @SpireInsertPatch( rloc = 43, localvars = {"damageAmount"})
        public static void Insert(AbstractPlayer _instance, DamageInfo info, @ByRef int[] damageAmount) {
            if (info.owner != AbstractDungeon.player) {
                return;
            }
            
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onAttack(info, damageAmount[0], _instance, true);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onAttack(info, damageAmount[0], _instance, false);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onAttack(info, damageAmount[0], _instance, false);
                }
            }
        }

        @SpireInsertPatch( rloc = 36, localvars = {"damageAmount"})
        public static void InsertAttacked(AbstractPlayer _instance, DamageInfo info, @ByRef int[] damageAmount) {
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    damageAmount[0] = ((ManiacRisksCard) card).onAttackedToModifyDamage(info, damageAmount[0], true);
                }
            }

            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    damageAmount[0] = ((ManiacRisksCard) card).onAttackedToModifyDamage(info, damageAmount[0], false);
                }
            }

            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    damageAmount[0] = ((ManiacRisksCard) card).onAttackedToModifyDamage(info, damageAmount[0], false);
                }
            }
        }
    }

    @SpirePatch( clz = AbstractMonster.class, method = "damage" )
    public static class onAttackMonster {

        @SpireInsertPatch( rloc = 49, localvars = {"damageAmount"})
        public static void Insert(AbstractMonster _instance, DamageInfo info, @ByRef int[] damageAmount) {
            if (info.owner != AbstractDungeon.player) {
                return;
            }

            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onAttack(info, damageAmount[0], _instance, true);
                }
            }

            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onAttack(info, damageAmount[0], _instance, false);
                }
            }

            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onAttack(info, damageAmount[0], _instance, false);
                }
            }
        }
    }
    
    @SpirePatch( clz = AbstractCard.class, method = "applyPowers" )
    public static class onAttackToApplyPowers {
        
        @SpireInsertPatch( rloc = 29, localvars = {"tmp"})
        public static void InsertDamage(AbstractCard _instance, @ByRef float[] tmp) {

            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    tmp[0] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[0], _instance.damageTypeForTurn, true);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    tmp[0] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[0], _instance.damageTypeForTurn, false);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    tmp[0] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[0], _instance.damageTypeForTurn, false);
                }
            }
        }

        @SpireInsertPatch( rloc = 76, localvars = {"tmp"})
        public static void InsertMultiDamage(AbstractCard _instance, float[] tmp) {
            
            for (int i = 0; i < tmp.length; i++) {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card instanceof ManiacRisksCard) {
                        tmp[i] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[i], _instance.damageTypeForTurn, true);
                    }
                }
                
                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (card instanceof ManiacRisksCard) {
                        tmp[i] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[i], _instance.damageTypeForTurn, false);
                    }
                }
                
                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (card instanceof ManiacRisksCard) {
                        tmp[i] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[i], _instance.damageTypeForTurn, false);
                    }
                }
            }
        }
    }
    
    @SpirePatch( clz = AbstractCard.class, method = "calculateCardDamage" )
    public static class onAttackToCalculateDamage {
        
        @SpireInsertPatch(rloc = 39, localvars = {"tmp"})
        public static void InsertDamage(AbstractCard _instance, AbstractMonster mo, @ByRef float[] tmp) {
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    tmp[0] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[0], _instance.damageTypeForTurn, true);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    tmp[0] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[0], _instance.damageTypeForTurn, false);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    tmp[0] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[0], _instance.damageTypeForTurn, false);
                }
            }
        }
        
        @SpireInsertPatch(rloc = 97, localvars = {"tmp"})
        public static void InsertMultiDamage(AbstractCard _instance, AbstractMonster mo, float[] tmp) {
            for (int i = 0; i < tmp.length; i++) {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card instanceof ManiacRisksCard) {
                        tmp[i] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[i], _instance.damageTypeForTurn, true);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (card instanceof ManiacRisksCard) {
                        tmp[i] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[i], _instance.damageTypeForTurn, false);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (card instanceof ManiacRisksCard) {
                        tmp[i] = ((ManiacRisksCard) card).onAttackToModifyDamage(tmp[i], _instance.damageTypeForTurn, false);
                    }
                }
            }
        }
    }
}
