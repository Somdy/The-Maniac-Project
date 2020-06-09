package TheManiac.actions.ThePossessedAction;

import TheManiac.actions.ApplyRandomDebuffAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class RiskUniversalThrillActions extends AbstractGameAction {
    private AbstractPlayer p;
    private AbstractMonster m;
    private AbstractCard.CardTarget target;
    private int chance; private int baseDmg; private int baseBlock; private int baseMagics;
    private boolean atMonster; private boolean atMonsters; private boolean atPlayer; private boolean atNone;
    
    public RiskUniversalThrillActions(AbstractPlayer p, AbstractMonster m, int chance, int baseMagics, int baseDmg, int baseBlock, AbstractCard.CardTarget target) {
        this.p = p;
        this.m = m;
        this.chance = chance;
        this.baseMagics = baseMagics;
        this.baseDmg = baseDmg;
        this.baseBlock = baseBlock;
        this.atMonster = false;
        this.atMonsters = false;
        this.atPlayer = false;
        this.atNone = false;
        this.target = target;
        if (this.baseDmg > -1)
            this.actionType = ActionType.DAMAGE;
        else 
            this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    public RiskUniversalThrillActions(AbstractPlayer p, AbstractMonster m, int chance, int baseMagics, int baseDmg, AbstractCard.CardTarget target) {
        this(p, m, chance, baseMagics, baseDmg, 0, target);
    }

    public RiskUniversalThrillActions(AbstractPlayer p, AbstractMonster m, int chance, int baseMagics, AbstractCard.CardTarget target) {
        this(p, m, chance, baseMagics, 0, 0, target);
    }
    
    @Override
    public void update() {
        if (target == AbstractCard.CardTarget.ENEMY || target == AbstractCard.CardTarget.SELF_AND_ENEMY)
            atMonster = true;
        if (target == AbstractCard.CardTarget.ALL_ENEMY || target == AbstractCard.CardTarget.ALL)
            atMonsters = true;
        if (target == AbstractCard.CardTarget.SELF || target == AbstractCard.CardTarget.ALL || target == AbstractCard.CardTarget.SELF_AND_ENEMY)
            atPlayer = true;
        if (target == AbstractCard.CardTarget.NONE)
            atNone = true;
        
        if (atMonster && m == null)
            m = AbstractDungeon.getMonsters().getRandomMonster(true);
        
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (atMonster && atPlayer) {
                if (chance < 11) {
                    int baseDamage = baseDmg > 0 ? baseDmg : (int) (m.currentHealth * 0.1F);
                    this.addToBot(new DamageAction(p, new DamageInfo(m, baseDamage, DamageInfo.DamageType.NORMAL), AttackEffect.BLUNT_HEAVY));
                }
                else if (chance < 22)  {
                    int baseMagic = baseMagics > 0 ? baseMagics : m.powers.size();
                    this.addToBot(new ApplyRandomDebuffAction(m, p, 1, true, baseMagic));
                }
                else if (chance < 33) {
                    int baseBlocks = baseBlock > 0 ? baseBlock : p.currentBlock;
                    if (baseBlocks <= 0)
                        baseBlocks += baseMagics + m.currentBlock;
                    this.addToBot(new GainBlockAction(p, p, baseBlocks));
                    this.addToBot(new GainBlockAction(m, m, baseBlocks * 2));
                }
                else if (chance < 44) {
                    if (m.currentHealth > p.currentHealth)
                        this.addToBot(new DamageAction(m, new DamageInfo(p, Math.abs(m.currentHealth - p.currentHealth), DamageInfo.DamageType.THORNS), AttackEffect.BLUNT_HEAVY));
                    else if (m.currentHealth < p.currentHealth)
                        this.addToBot(new DamageAction(p, new DamageInfo(m, Math.abs(m.currentHealth - p.currentHealth), DamageInfo.DamageType.THORNS), AttackEffect.BLUNT_HEAVY));
                }
            }
            else if (atMonsters && !atPlayer) {
                if (chance < 12) {
                    int amounts = baseMagics > 0 ? baseMagics : m.powers.size();
                    this.addToBot(new ApplyPowerAction(m, p, new IntangiblePower(m, amounts), amounts));
                }
                else if (chance < 23) {
                    int blocks = baseBlock > 0 ? baseBlock : baseMagics + baseDmg;
                    if (blocks <= 0)
                        blocks += 10;
                    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (!monster.isDeadOrEscaped()) {
                            this.addToBot(new GainBlockAction(m, m, blocks));
                        }
                    }
                }
                else if (chance < 34) {
                    int amounts = baseMagics > 0 ? baseMagics : p.powers.size();
                    this.addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, amounts), amounts));
                }
                else if (chance < 45) {
                    int healAmt = baseMagics > 0 ? baseMagics : (int) (m.maxHealth * 0.1F);
                    this.addToBot(new HealAction(m, m, healAmt));
                }
            }
            else if (atPlayer && !atMonsters) {
                if (chance < 21) {
                    int loseAmt = baseBlock > 0 ? baseBlock : baseMagics + baseDmg;
                    if (loseAmt <= 0)
                        loseAmt += 10;
                    this.addToBot(new LoseBlockAction(p, p, loseAmt));
                }
                else if (chance < 32) {
                    int amounts = baseMagics > 0 ? baseMagics : baseBlock + baseDmg;
                    if (amounts <= 0)
                        amounts += 2;
                    this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -amounts), -amounts));
                }
                else if (chance < 43) {
                    if (!p.drawPile.isEmpty()) {
                        for (AbstractCard card : p.drawPile.group) {
                            p.drawPile.moveToDiscardPile(card);
                        }
                    }
                    if (p.exhaustPile.size() > baseMagics) {
                        for (AbstractCard card : p.exhaustPile.group) {
                            if (AbstractDungeon.cardRandomRng.randomBoolean())
                                p.exhaustPile.moveToDiscardPile(card);
                        }
                    }
                }
            }
            else if (atMonsters) {
                if (chance < 23) {
                    int damage = baseDmg > 0 ? baseDmg : (int) (p.currentHealth * 0.1F);
                    this.addToBot(new VFXAction(new CleaveEffect()));
                    this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(damage , false, false), DamageInfo.DamageType.NORMAL, AttackEffect.NONE));
                }
                else if (chance < 34) {
                    int amounts = baseMagics > 0 ? baseMagics : baseBlock + baseDmg;
                    if (amounts <= 0)
                        amounts += 4;
                    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (!monster.isDeadOrEscaped())
                            this.addToBot(new ApplyPowerAction(monster, monster, new MetallicizePower(monster, amounts), amounts));
                    }
                }
                else if (chance < 45) {
                    int amounts = baseMagics > 0 ? baseMagics : baseBlock + baseDmg;
                    if (amounts <= 0)
                        amounts += 2;
                    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (!monster.isDeadOrEscaped())
                            this.addToBot(new ApplyPowerAction(monster, monster, new RegenerateMonsterPower(monster, amounts), amounts));
                    }
                }
            }
            else if (atNone) {
                this.isDone = true;
                return;
            }
        }
        
        this.tickDuration();
    }
}
