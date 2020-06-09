package TheManiac.monsters.enemies;

import TheManiac.TheManiac;
import TheManiac.powers.BloodsuckingPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.SetAnimationAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class JawFlesh extends CustomMonster {
    public static final String ID = "maniac:jawFlesh";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String JAW_ATLAS = "maniacMod/images/monsters/enemies/jawFlesh/jawFlesh.atlas";
    private static final String JAW_JSON = "maniacMod/images/monsters/enemies/jawFlesh/skeleton.json";
    private static final int max_hp = 90;
    private static final int min_hp = 84;
    private static final int asc_maxHp = 100;
    private static final int asc_minHp = 94;
    private static final int str = 3;
    private static final int asc_str = 5;
    private int bellowArmor;
    private int thrashArmor;
    private int chompDmg;
    private int thrashDmg;
    private boolean firstMove;
    
    public JawFlesh(float x, float y) {
        super(NAME, ID, 72, 0F, -25F, 260F, 170F, null, x, y);
        this.firstMove = true;

        if (AbstractDungeon.ascensionLevel >= 7) {
            if (TheManiac.challengerMode) {
                this.setHp(asc_minHp + 1, asc_maxHp + 1);
            } else {
                this.setHp(asc_minHp, asc_maxHp);
            }
        } else {
            if (TheManiac.challengerMode) {
                this.setHp(min_hp + 1, max_hp + 1);
            } else {
                this.setHp(min_hp, max_hp);
            }
        }
        
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (TheManiac.challengerMode) {
                this.bellowArmor = 6;
            } else {
                this.bellowArmor = 5;
            }
        } else {
            this.bellowArmor = 3;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.thrashArmor = 4;
        } else {
            this.thrashArmor = 3;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.chompDmg = 11;
        } else {
            this.chompDmg = 10;
        }
        
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.thrashDmg = 7;
        } else {
            this.thrashDmg = 6;
        }
        this.damage.add(new DamageInfo(this, this.chompDmg));
        this.damage.add(new DamageInfo(this, this.thrashDmg));
        
        this.loadAnimation(JAW_ATLAS, JAW_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (TheManiac.challengerMode) {
                this.addToBot(new ApplyPowerAction(this, this, new BloodsuckingPower(this, 5), 5));
            } else {
                this.addToBot(new ApplyPowerAction(this, this, new BloodsuckingPower(this, 4), 4));
            }
        } else {
            if (TheManiac.challengerMode) {
                this.addToBot(new ApplyPowerAction(this, this, new BloodsuckingPower(this, 3), 3));
            } else {
                this.addToBot(new ApplyPowerAction(this, this, new BloodsuckingPower(this, 2), 2));
            }
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                this.addToBot(new SetAnimationAction(this, "chomp"));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case 2:
                this.addToBot(new AnimateHopAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.thrashArmor), this.thrashArmor));
                break;
            case 3:
                this.state.setAnimation(0, "tailslam", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                this.addToBot(new SFXAction("MONSTER_JAW_WORM_BELLOW"));
                this.addToBot(new ShakeScreenAction(0.2F, ScreenShake.ShakeDur.SHORT, ScreenShake.ShakeIntensity.MED));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, asc_str), asc_str));
                } else {
                    this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, str), str));
                }
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            this.setMove(MOVES[0], (byte)3, Intent.DEFEND_BUFF);
        } else {
            if (AbstractDungeon.ascensionLevel >= 17) {
                if (num < 20) {
                    if (this.lastMove((byte)3)) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.45F)) {
                            this.setMove(MOVES[1], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                        } else {
                            this.setMove(MOVES[2], (byte)1, Intent.ATTACK, this.damage.get(0).base);
                        }
                    } else {
                        this.setMove(MOVES[0], (byte)3, Intent.DEFEND_BUFF);
                    }
                }
                else if (num < 35) {
                    if (this.lastMove((byte)2)) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.35F)) {
                            this.setMove(MOVES[0], (byte)3, Intent.DEFEND_BUFF);
                        } else {
                            this.setMove(MOVES[2], (byte)1, Intent.ATTACK, this.damage.get(0).base);
                        }
                    } else {
                        this.setMove(MOVES[1], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                    }
                } else {
                    if (this.lastMove((byte)1)) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.30F)) {
                            this.setMove(MOVES[0], (byte)3, Intent.DEFEND_BUFF);
                        } else {
                            this.setMove(MOVES[1], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                        }
                    } else {
                        this.setMove(MOVES[2], (byte)1, Intent.ATTACK, this.damage.get(0).base);
                    }
                }
            } 
            else {
                if (num < 25) {
                    if (this.lastMove((byte)2)) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.35F)) {
                            this.setMove(MOVES[0], (byte)3, Intent.DEFEND_BUFF);
                        } else {
                            this.setMove(MOVES[2], (byte)1, Intent.ATTACK, this.damage.get(0).base);
                        }
                    } else {
                        this.setMove(MOVES[1], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                    }
                    
                }
                else if (num < 35) {
                    if (this.lastMove((byte)3)) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.45F)) {
                            this.setMove(MOVES[1], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                        } else {
                            this.setMove(MOVES[2], (byte)1, Intent.ATTACK, this.damage.get(0).base);
                        }
                    } else {
                        this.setMove(MOVES[0], (byte)3, Intent.DEFEND_BUFF);
                    }
                } else {
                    if (this.lastMove((byte)1)) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.30F)) {
                            this.setMove(MOVES[0], (byte)3, Intent.DEFEND_BUFF);
                        } else {
                            this.setMove(MOVES[1], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                        }
                    } else {
                        this.setMove(MOVES[2], (byte)1, Intent.ATTACK, this.damage.get(0).base);
                    }
                }
            }
        }
    }

    @Override
    public void die() {
        super.die();
        CardCrawlGame.sound.play("JAW_WORM_DEATH");
    }
}
