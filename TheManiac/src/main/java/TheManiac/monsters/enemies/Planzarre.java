package TheManiac.monsters.enemies;

import TheManiac.powers.WeaknessPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class Planzarre extends CustomMonster {
    public static final String ID = "maniac:Planzarre";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String PLANT_ATLAS = "maniacMod/images/monsters/enemies/Planzarre/skeleton.atlas";
    private static final String PLANT_JSON = "maniacMod/images/monsters/enemies/Planzarre/skeleton.json";
    private static final int max_hp = 174;
    private static final int min_hp = 170;
    private static final int asc_maxHp = 180;
    private static final int asc_minHp = 176;
    private static final int spore_frail = 2;
    private static final int attacks = 3;
    private int pre_malleable;
    private int chomp_dmg;
    private int bite_dmg;
    private int spore_weakness;
    private boolean firstMove;
    
    public Planzarre(float x, float y) {
        super(NAME, ID, 174, 0F, -44F, 350F, 360F, null, x, y + 50F);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(asc_minHp, asc_maxHp);
            this.pre_malleable = 3;
        } else {
            this.pre_malleable = 2;
            this.setHp(min_hp, max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.spore_weakness = 8;
        } else {
            this.spore_weakness = 4;
        }

        this.firstMove = AbstractDungeon.ascensionLevel >= 18;
        
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.chomp_dmg = 7;
            this.bite_dmg = 5;
        } else {
            this.chomp_dmg = 6;
            this.bite_dmg = 4;
        }
        this.damage.add(new DamageInfo(this, this.chomp_dmg));
        this.damage.add(new DamageInfo(this, this.bite_dmg));
        
        this.loadAnimation(PLANT_ATLAS, PLANT_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.8F);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MalleablePower(this, pre_malleable)));
    }

    @Override
    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 1:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.5F));
                
                for (i = 0; i < attacks; i++) {
                    this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.ORANGE.cpy()), 0.2F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE, true));
                }
                break;
            case 2:
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, spore_frail, true), spore_frail));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeaknessPower(AbstractDungeon.player, spore_weakness), spore_weakness));
                break;
            case 3:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.5F));

                for (i = 0; i < attacks; i++) {
                    this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.SALMON.cpy()), 0.2F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE, true));
                    this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                }
                break;
            case 4:
                this.addToBot(new ApplyPowerAction(this, this, new MalleablePower(this, 1), 1));
                this.addToBot(new HealAction(this, this, 6));
                break;
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (firstMove) {
            firstMove = false;
            this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
        } else {
            if (AbstractDungeon.ascensionLevel >= 18) {
                if (num < 15) {
                    if (!this.lastTwoMoves((byte)4)) {
                        this.setMove(MOVES[2], (byte)4, Intent.BUFF);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.45F)) {
                            this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, attacks, true);
                        } else {
                            this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
                        }
                    }
                }
                else if (num < 20) {
                    if (!this.lastMove((byte)2)) {
                        this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.65F)) {
                            this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, attacks, true);
                        } else {
                            this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
                        }
                    }
                }
                else if (num < 30) {
                    if (!this.lastMove((byte)1)) {
                        this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, attacks, true);
                    } else {
                        this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
                    }
                } else {
                    if (!this.lastMove((byte)3)) {
                        this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
                    } else {
                        this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                    }
                }
            } else {
                if (num < 25) {
                    if (this.lastMove((byte)2)) {
                        this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.35F)) {
                            this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
                        } else {
                            this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, attacks, true);
                        }
                    }
                }
                else if (num < 35) {
                    if (this.lastTwoMoves((byte)1)) {
                        this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, attacks, true);
                    } else {
                        if (AbstractDungeon.aiRng.randomBoolean(0.45F)) {
                            this.setMove(MOVES[0], (byte)2, Intent.DEBUFF);
                        } else {
                            this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
                        }
                    }
                } else {
                    if (!this.lastMove((byte)3)) {
                        this.setMove(MOVES[1], (byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, attacks, true);
                    } else {
                        this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, attacks, true);
                    }
                }
            }
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
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
        }
    }
}
