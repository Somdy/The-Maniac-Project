package TheManiac.monsters.enemies;

import TheManiac.actions.SneckouetteSummonAction;
import TheManiac.powers.ExoticPoisonPower;
import TheManiac.powers.PuzzledPower;
import TheManiac.powers.SilhouettePower;
import TheManiac.vfx.SneckouetteSummonEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;

public class Sneckouette extends CustomMonster {
    public static final String ID = "maniac:Sneckouette";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String SNECKO_ATLAS = "maniacMod/images/monsters/enemies/Sneckouette/Sneckouette.atlas";
    private static final String SNECKO_JSON = "maniacMod/images/monsters/enemies/Sneckouette/skeleton.json";
    private static final int max_hp = 246;
    private static final int asc_maxHp = 256;
    private boolean isDupe;
    private boolean isSummoned;
    private boolean firstMove;
    private boolean summonFirstMove;
    private boolean poison_pureDmg;
    private int perplex_power;
    private int summon_power;
    private int evolve_str;
    private int evolve_times;
    private int evolve_block;
    private int bite_dmg;
    private int fang_dmg;
    private int fang_poisonAmt;
    private int hiss_power;
    private int sub_hp;
    private int summon_hp;
    private int harden_block;
    
    public Sneckouette(float x, float y, boolean isSilhouette, boolean isSummoned, int summonHp) {
        super(NAME, ID, 246, -30.0F, -20.0F, 310.0F, 305.0F, null, x, y);
        this.isDupe = isSilhouette;
        this.isSummoned = isSummoned;
        this.evolve_times = 0;
        this.type = EnemyType.BOSS;
        
        if (this.isSummoned) {
            this.firstMove = false;
            this.summonFirstMove = true;
            this.setHp(summonHp);
        } else {
            this.firstMove = true;
            this.summonFirstMove = false;
            if (AbstractDungeon.ascensionLevel >= 9) {
                this.setHp(asc_maxHp);
            } else {
                this.setHp(max_hp);
            }
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.poison_pureDmg = true;
            this.harden_block = 22;
            this.summon_power = 2;
            this.hiss_power = 4;
            this.evolve_str = 2;
            this.evolve_block = 14;
            this.fang_poisonAmt = 3;
            this.perplex_power = 5;
        }
        else if (AbstractDungeon.ascensionLevel >= 9) {
            this.harden_block = 18;
            this.evolve_block = 12;
            this.sub_hp = 20;
            this.summon_hp = 85;
        } else {
            this.poison_pureDmg = false;
            this.harden_block = 16;
            this.summon_power = 3;
            this.hiss_power = 2;
            this.evolve_str = 1;
            this.evolve_block = 10;
            this.fang_poisonAmt = 2;
            this.perplex_power = 3;
            this.sub_hp = 10;
            this.summon_hp = 78;
        }
        
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.bite_dmg = 11;
            this.fang_dmg = 7;
        } else {
            this.bite_dmg = 10;
            this.fang_dmg = 6;
        }
        this.damage.add(new DamageInfo(this, this.bite_dmg));
        this.damage.add(new DamageInfo(this, this.fang_dmg));
        
        this.loadAnimation(SNECKO_ATLAS, SNECKO_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.8F);
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
    }

    @Override
    public void usePreBattleAction() {
        if (!this.isSummoned) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");
            UnlockTracker.markBossAsSeen(this.id);
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                int powers;
                this.addToBot(new TalkAction(this, DIALOG[0]));
                this.addToBot(new VFXAction(new SneckouetteSummonEffect(this.hb.cX + 150F, this.hb.cY)));
                this.addToBot(new VFXAction(new SneckouetteSummonEffect(this.hb.cX - 200F, this.hb.cY)));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    powers = 3;
                } else {
                    powers = 4;
                }
                this.addToBot(new SneckouetteSummonAction(this.hb_x, this.hb_y, powers, this.summon_hp));
                System.out.println("Get summon powers: " + powers);
                this.setMove(MOVES[1], (byte)2, Intent.STRONG_DEBUFF);
                break;
            case 2:
                this.addToBot(new TalkAction(this, DIALOG[1]));
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new SFXAction("MONSTER_SNECKO_GLARE"));
                this.addToBot(new VFXAction(this, new IntimidateEffect(this.hb.cX, this.hb.cY), 0.5F));
                this.addToBot(new FastShakeAction(AbstractDungeon.player, 1.0F, 1.0F));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.perplex_power = 5;
                } else {
                    this.perplex_power = 3;
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new PuzzledPower(AbstractDungeon.player, this.perplex_power), this.perplex_power));
                this.setMove(MOVES[2], (byte)3, Intent.DEFEND_BUFF);
                break;
            case 3:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.evolve_str = 2;
                    this.evolve_block = 7;
                } else {
                    this.evolve_str = 1;
                    this.evolve_block = 6;
                }
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        this.addToBot(new ApplyPowerAction(this, this, 
                                new StrengthPower(this, (this.evolve_str + this.evolve_times)), (this.evolve_str + this.evolve_times)));
                        this.addToBot(new GainBlockAction(this, this, (this.evolve_block + this.evolve_times), true));
                        if (AbstractDungeon.ascensionLevel >= 9) {
                            this.addToBot(new ApplyPowerAction(m, this, new PlatedArmorPower(m, 1), 1));
                        }
                        continue;
                    }
                    if (!m.isDying) {
                        this.addToBot(new ApplyPowerAction(m, this, 
                                new StrengthPower(m, (this.evolve_str + this.evolve_times)), (this.evolve_str + this.evolve_times)));
                        this.addToBot(new GainBlockAction(m, this, (this.evolve_block + this.evolve_times), true));
                        if (AbstractDungeon.ascensionLevel >= 9) {
                            this.addToBot(new ApplyPowerAction(m, this, new PlatedArmorPower(m, 2), 2));
                        }
                    }
                }
                this.evolve_times++;
                this.addToBot(new RollMoveAction(this));
                break;
            case 4:
                this.addToBot(new ChangeStateAction(this, "ATTACK_2"));
                this.addToBot(new WaitAction(0.3F));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale, 
                        AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.ROYAL.cpy()), 0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                this.addToBot(new RollMoveAction(this));
                break;
            case 5:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.poison_pureDmg = true;
                    this.fang_poisonAmt = 4;
                } else {
                    this.poison_pureDmg = false;
                    this.fang_poisonAmt = 2;
                }
                this.addToBot(new ChangeStateAction(this, "ATTACK_2"));
                this.addToBot(new WaitAction(0.3F));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale,
                        AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.PURPLE.cpy()), 0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new ExoticPoisonPower(AbstractDungeon.player, this, this.fang_poisonAmt, this.poison_pureDmg), this.fang_poisonAmt));
                this.addToBot(new RollMoveAction(this));
                break;
            case 6:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new SFXAction("MONSTER_SNECKO_GLARE"));
                this.addToBot(new FastShakeAction(AbstractDungeon.player, 1.0F, 1.0F));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.hiss_power = 3;
                } else {
                    this.hiss_power = 2;
                }
                int power = AbstractDungeon.cardRandomRng.random(0, 2);
                switch (power) {
                    case 1:
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.hiss_power, true), this.hiss_power));
                        break;
                    case 2:
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.hiss_power, true), this.hiss_power));
                        break;
                    default:
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.hiss_power, true), this.hiss_power));
                }
                this.addToBot(new RollMoveAction(this));
                break;
            case 7:
                this.increaseMaxHp(this.sub_hp, true);
                this.heal(this.maxHealth, true);
                this.updateHealthBar();
                this.isDupe = false;
                this.setMove(MOVES[7], (byte)8, Intent.DEFEND);
                break;
            case 8:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.harden_block = 10;
                }
                else if (AbstractDungeon.ascensionLevel >= 9) {
                    this.harden_block = 8;
                } else {
                    this.harden_block = 6;
                }
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        this.addToBot(new GainBlockAction(this, this, this.harden_block, true));
                        continue;
                    }
                    if (!m.isDying) {
                        this.addToBot(new GainBlockAction(m, this, this.harden_block, true));
                    }
                }
                this.addToBot(new RollMoveAction(this));
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        if (!this.isSummoned) {
            if (this.firstMove) {
                this.firstMove = false;
                this.setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
            } else {
                if (num < 35) {
                    if (!this.lastMove((byte)3)) {
                        this.setMove(MOVES[2], (byte)3, Intent.DEFEND_BUFF);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.35F)) {
                            this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                        } else {
                            this.setMove(MOVES[4], (byte)5, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                        }
                    }
                }
                else if (num < 55) {
                    if (!this.lastTwoMoves((byte)4)) {
                        this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.66F)) {
                            this.setMove(MOVES[4], (byte)5, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                        } else {
                            this.setMove(MOVES[2], (byte)3, Intent.DEFEND_BUFF);
                        }
                    }
                } else {
                    if (!this.lastMove((byte)5)) {
                        this.setMove(MOVES[4], (byte)5, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.44F)) {
                            this.setMove(MOVES[2], (byte)3, Intent.DEFEND_BUFF);
                        } else {
                            this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                        }
                    }
                }
            }
        } else {
            if (this.isDupe) {
                if (this.summonFirstMove) {
                    this.summonFirstMove = false;
                    this.setMove(MOVES[5], (byte)6, Intent.DEBUFF);
                } else {
                    AbstractPower power = this.getPower(SilhouettePower.POWER_ID);
                    if (power.amount > 1) {
                        if (num < 55) {
                            if (!this.lastMove((byte)6)) {
                                this.setMove(MOVES[5], (byte)6, Intent.DEBUFF);
                            }
                            else {
                                this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                            }
                        } else {
                            if (!this.lastMove((byte)4)) {
                                this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                            }
                            else {
                                this.setMove(MOVES[5], (byte)6, Intent.DEBUFF);
                            }
                        }
                    }
                    else if (power.amount == 1) {
                        this.isDupe = false;
                        this.setMove(MOVES[6], (byte)7, Intent.UNKNOWN);
                    }
                }
            } else {
                if (num < 23) {
                    if (!this.lastTwoMoves((byte)5)) {
                        this.setMove(MOVES[4], (byte)5, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.65F)) {
                            this.setMove(MOVES[7], (byte)8, Intent.DEFEND);
                        } else {
                            this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                        }
                    }
                }
                else if (num < 34) {
                    if (!this.lastMove((byte)3)) {
                        this.setMove(MOVES[2], (byte)3, Intent.DEFEND_BUFF);
                    } else {
                        if (!this.lastMoveBefore((byte)5)) {
                            this.setMove(MOVES[4], (byte)5, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                        } else {
                            this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                        }
                    }
                } else {
                    if (!this.lastMoveBefore((byte)4)) {
                        this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.37F)) {
                            this.setMove(MOVES[2], (byte)3, Intent.DEFEND_BUFF);
                        } else {
                            this.setMove(MOVES[7], (byte)8, Intent.DEFEND);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
            case "ATTACK_2":
                this.state.setAnimation(0, "Attack_2", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void die() {
        super.die();
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            CardCrawlGame.sound.play("SNECKO_DEATH");
            AbstractDungeon.scene.fadeInAmbiance();
            this.onBossVictoryLogic();
        }
    }
}
