package TheManiac.monsters.enemies;

import TheManiac.TheManiac;
import TheManiac.powers.BleedingPower;
import TheManiac.powers.CurlUpPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.WebEffect;

public class VariantLouse extends CustomMonster {
    public static final String ID = "maniac:variantLouse";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String LOUSE_ATLAS = "maniacMod/images/monsters/enemies/variantLouse/skeleton.atlas";
    private static final String LOUSE_JSON = "maniacMod/images/monsters/enemies/variantLouse/skeleton.json";
    private static final int max_hp = 30;
    private static final int min_hp = 26;
    private static final int asc_maxHp = 36;
    private static final int asc_minHp = 32;
    private static final int max_bite = 6;
    private static final int min_bite = 4;
    private static final int bleeding = 2;
    private static final int asc_bleeding = 3;
    private int biteDmg;
    private boolean isOpen = true;
    
    public VariantLouse(float x, float y) {
        super(NAME, ID, 15, 0F, -5F, 180F, 140F, null, x, y);
        
        if (AbstractDungeon.ascensionLevel >= 7) {
            if (TheManiac.challengerMode) {
                this.setHp(asc_minHp + 1, asc_maxHp + 1);
            } else {
                this.setHp(asc_minHp, asc_maxHp);
            }
        } else {
            this.setHp(min_hp, max_hp);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            if (TheManiac.challengerMode) {
                this.biteDmg = AbstractDungeon.monsterHpRng.random(min_bite + 1, max_bite + 2);
            } else {
                this.biteDmg = AbstractDungeon.monsterHpRng.random(min_bite + 1, max_bite + 1);
            }
        } else {
            this.biteDmg = AbstractDungeon.monsterHpRng.random(min_bite, max_bite);
        }
        this.damage.add(new DamageInfo(this, this.biteDmg));

        this.loadAnimation(LOUSE_ATLAS, LOUSE_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.addToBot(new ApplyPowerAction(this, this, new CurlUpPower(this, AbstractDungeon.monsterHpRng.random(8, 10), AbstractDungeon.monsterHpRng.random(2, 4))));
        }
        else if (AbstractDungeon.ascensionLevel >= 7) {
            this.addToBot(new ApplyPowerAction(this, this, new com.megacrit.cardcrawl.powers.CurlUpPower(this, AbstractDungeon.monsterHpRng.random(6, 8))));
        } else {
            this.addToBot(new ApplyPowerAction(this, this, new com.megacrit.cardcrawl.powers.CurlUpPower(this, AbstractDungeon.monsterHpRng.random(4, 6))));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                if (!isOpen) {
                    this.addToBot(new ChangeStateAction(this, "OPEN"));
                    this.addToBot(new WaitAction(0.5F));
                }

                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    if (TheManiac.challengerMode) {
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new BleedingPower(AbstractDungeon.player, asc_bleeding + 1), asc_bleeding + 1));
                    } else {
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new BleedingPower(AbstractDungeon.player, asc_bleeding), asc_bleeding));
                    }
                } else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new BleedingPower(AbstractDungeon.player, bleeding), bleeding));
                }
                break;
            case 2:
                if (!isOpen) {
                    this.addToBot(new ChangeStateAction(this, "REAR"));
                    this.addToBot(new WaitAction(1.2F));
                    this.addToBot(new SFXAction("ATTACK_MAGIC_FAST_3", MathUtils.random(0.88F, 0.92F), true));
                    this.addToBot(new VFXAction(new WebEffect(AbstractDungeon.player, this.hb.cX - 70.0F * Settings.scale, this.hb.cY + 10.0F * Settings.scale)));
                } else {
                    this.addToBot(new ChangeStateAction(this, "REAR_IDLE"));
                    this.addToBot(new WaitAction(0.9F));
                    this.addToBot(new SFXAction("ATTACK_MAGIC_FAST_3", MathUtils.random(0.88F, 0.92F), true));
                    this.addToBot(new VFXAction(new WebEffect(AbstractDungeon.player, this.hb.cX - 70.0F * Settings.scale, this.hb.cY + 10.0F * Settings.scale)));
                }

                if (AbstractDungeon.ascensionLevel >= 17) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                    if (TheManiac.challengerMode) {
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -2), -2));
                    } else {
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -1), -1));
                    }
                } else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                }
                break;
            case 3:
                if (!isOpen) {
                    this.addToBot(new ChangeStateAction(this, "REAR"));
                    this.addToBot(new WaitAction(1.2F));
                } else {
                    this.addToBot(new ChangeStateAction(this, "REAR_IDLE"));
                    this.addToBot(new WaitAction(0.9F));
                }
                int platedArmorAmt;
                if (TheManiac.challengerMode) {
                    platedArmorAmt = AbstractDungeon.monsterHpRng.random(2, 3);
                } else {
                    platedArmorAmt = AbstractDungeon.monsterHpRng.random(1, 2);
                }
                this.addToBot(new ApplyPowerAction(this, this, new CurlUpPower(this, AbstractDungeon.monsterHpRng.random(6, 8), platedArmorAmt)));
                break;
        }

        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (num < 25) {
                if (!this.hasPower(CurlUpPower.POWER_ID)) {
                    if (TheManiac.challengerMode) {
                        if (num < 15) {
                            this.setMove(MOVES[1], (byte)3, Intent.BUFF);
                        }
                        else if (this.lastMove((byte)2)) {
                            this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                        } else {
                            this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                        }
                    } else {
                        if (num < 10) {
                            this.setMove(MOVES[1], (byte)3, Intent.BUFF);
                        }
                        else if (this.lastMove((byte)2)) {
                            this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                        } else {
                            this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                        }
                    }
                }
                else if (this.lastMove((byte)2)) {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                } else {
                    this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                }
            }
            else if (this.lastMove((byte)1)) {
                this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
            } else {
                this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
            }
        } else {
            if (num < 15) {
                if (!this.hasPower(CurlUpPower.POWER_ID)) {
                    this.setMove(MOVES[1], (byte)3, Intent.BUFF);
                } else {
                    if (!this.lastMove((byte)1)) {
                        this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                    } else {
                        this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                    }
                }
            }
            else if (num < 25) {
                if (this.lastTwoMoves((byte)2)) {
                    this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
                } else {
                    this.setMove(MOVES[0], (byte)2, Intent.BUFF);
                }
            }
            else if (this.lastTwoMoves((byte)1)) {
                this.setMove(MOVES[0], (byte)2, Intent.BUFF);
            } else {
                this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
            }
        }
    }

    @Override
    public void changeState(String stateName) {
        if (stateName.equals("CLOSED")) {
            this.state.setAnimation(0, "transitiontoclosed", false);
            this.state.addAnimation(0, "idle closed", true, 0.0F);
            this.isOpen = false;
        } else if (stateName.equals("OPEN")) {
            this.state.setAnimation(0, "transitiontoopened", false);
            this.state.addAnimation(0, "idle", true, 0.0F);
            this.isOpen = true;
        } else if (stateName.equals("REAR_IDLE")) {
            this.state.setAnimation(0, "rear", false);
            this.state.addAnimation(0, "idle", true, 0.0F);
            this.isOpen = true;
        } else {
            this.state.setAnimation(0, "transitiontoopened", false);
            this.state.addAnimation(0, "rear", false, 0.0F);
            this.state.addAnimation(0, "idle", true, 0.0F);
            this.isOpen = true;
        }
    }
}
