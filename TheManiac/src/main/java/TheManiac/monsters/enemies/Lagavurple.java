package TheManiac.monsters.enemies;

import TheManiac.powers.PetrifyPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Lagavurple extends CustomMonster {
    public static final String ID = "maniac:Lagavurple";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String LAGAP_ATLAS = "maniacMod/images/monsters/enemies/Lagavurple/Lagavurple.atlas";
    private static final String LAGAP_JSON = "maniacMod/images/monsters/enemies/Lagavurple/skeleton.json";
    private static final int max_hp = 78;
    private static final int min_hp = 74;
    private static final int asc_maxHp = 84;
    private static final int asc_minHp = 80;
    private static final int siphon_weak = 1;
    private static final int siphon_str = 1;
    private static final int asc_weak = 2;
    private static final int asc_str = 2;
    private int scratch_dmg;
    private int drain_dmg;
    private int defend_counter;
    private int attack_counter;
    private int defend_metal;
    private int dmgTaken;
    private int dmgHold;
    private boolean isDefensive;
    private boolean firstMove;
    
    public Lagavurple(float x, float y) {
        super(NAME, ID, 78, 0.0F, -25.0F, 320.0F, 360.0F, null, x, y);
        this.type = EnemyType.ELITE;
        this.firstMove = true;
        this.isDefensive = false;
        this.defend_counter = 0;
        this.attack_counter = 0;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(asc_minHp, asc_maxHp);
        } else {
            this.setHp(min_hp, max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.dmgHold = 30;
            this.defend_metal = 10;
        } else {
            this.dmgHold = 40;
            this.defend_metal = 8;
        }
        
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.scratch_dmg = 14;
            this.drain_dmg = 4;
        } else {
            this.scratch_dmg = 12;
            this.drain_dmg = 3;
        }
        this.damage.add(new DamageInfo(this, this.scratch_dmg));
        this.damage.add(new DamageInfo(this, this.drain_dmg));
        
        this.loadAnimation(LAGAP_ATLAS, LAGAP_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle_2", true);
        this.stateData.setMix("Attack", "Idle_2", 0.25F);
        this.stateData.setMix("Hit", "Idle_2", 0.25F);
        this.stateData.setMix("Idle_1", "Idle_2", 0.5F);
        this.stateData.setMix("Idle_2", "Idle_1", 0.25F);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new PetrifyPower(this, dmgHold)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                this.attack_counter = 0;
                this.addToBot(new ChangeStateAction(this, "DEBUFF"));
                this.addToBot(new WaitAction(0.3F));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, asc_weak, true), asc_weak));
                    this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, asc_str), asc_str));
                } else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, siphon_weak, true), siphon_weak));
                    this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, siphon_str), siphon_str));
                }
                break;
            case 2:
                this.attack_counter++;
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 3:
                this.defend_counter++;
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new VampireDamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addToBot(new VampireDamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 4:
                this.defend_counter++;
                this.addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, defend_metal), defend_metal));
                break;
            case 5:
                this.addToBot(new ChangeStateAction(this, "OPEN"));
                this.addToBot(new ApplyPowerAction(this, this, new PetrifyPower(this, dmgHold)));
                if (this.hasPower(MetallicizePower.POWER_ID)) {
                    this.addToBot(new RemoveSpecificPowerAction(this, this, MetallicizePower.POWER_ID));
                }
                break;
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (!this.isDefensive) {
            if (this.firstMove) {
                this.firstMove = false;
                this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
            } else {
                if (this.lastMove((byte)5)) {
                    this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
                }
                else if (this.attack_counter < 2) {
                    this.setMove(MOVES[1], (byte)2, Intent.ATTACK, this.damage.get(0).base);
                } else {
                    this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
                }
            }
        } else {
            if (this.lastMove((byte)4)) {
                this.setMove((byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, 2, true);
            }
            else if (this.lastMove((byte)3) && this.defend_counter <= 2) {
                this.setMove((byte)3, Intent.ATTACK_BUFF, this.damage.get(1).base, 2, true);
            }
            else if (this.defend_counter > 2) {
                this.setMove((byte)5, Intent.UNKNOWN);
            }
        }
    }

    @Override
    public void changeState(String stateName) {
        if (stateName.equals("ATTACK")) {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        } else if (stateName.equals("DEBUFF")) {
            this.state.setAnimation(0, "Debuff", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        } else if (stateName.equals("OPEN") && !this.isDying) {
            this.isDefensive = false;
            this.updateHitbox(0.0F, -25.0F, 320.0F, 360.0F);
            this.state.setAnimation(0, "Coming_out", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        } else if (stateName.equals("DEFEND") && !this.isDying) {
            this.isDefensive = true;
            this.defend_counter = 0;
            this.updateHitbox(0.0F, -25.0F, 320.0F, 220.0F);
            this.state.addAnimation(0, "Idle_1", true, 0F);
            this.setMove((byte)4, Intent.BUFF);
            this.createIntent();
        }
    }

    @Override
    public void damage(DamageInfo info) {
        int tmpHP = this.currentHealth;
        super.damage(info);
        
        if (!this.isDefensive && info.output > 0 && info.type == DamageInfo.DamageType.NORMAL && info.owner != null) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        }
        
        if (!this.isDefensive && tmpHP > this.currentHealth && !this.isDying) {
            this.dmgTaken += tmpHP - this.currentHealth;
            if (this.getPower(PetrifyPower.POWER_ID) != null) {
                (this.getPower(PetrifyPower.POWER_ID)).amount -= tmpHP - this.currentHealth;
                (this.getPower(PetrifyPower.POWER_ID)).updateDescription();
            }
            
            if (this.dmgTaken >= this.dmgHold) {
                this.dmgTaken = 0;
                this.addToBot(new ChangeStateAction(this, "DEFEND"));
                this.addToBot(new RemoveSpecificPowerAction(this, this, PetrifyPower.POWER_ID));
            }
        }
    }
}
