package TheManiac.monsters.enemies;

import TheManiac.TheManiac;
import TheManiac.actions.CorruardianSummonAction;
import TheManiac.powers.CorruardianSelfDestructionPower;
import TheManiac.powers.NoAttackPower;
import TheManiac.vfx.CorruardianOrbsEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class Corruardian extends CustomMonster {
    public static final String ID = "maniac:Corruardian";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String CORAD_ATLAS = "maniacMod/images/monsters/enemies/Corruardian/skeleton.atlas";
    private static final String CORAD_JSON = "maniacMod/images/monsters/enemies/Corruardian/skeleton.json";
    private static final String ORBS_ATLAS = "maniacMod/images/monsters/enemies/CorruardianOrbs/skeleton.atlas";
    private static final String ORBS_JSON = "maniacMod/images/monsters/enemies/CorruardianOrbs/skeleton.json";
    private static final int max_hp = 60;
    private static final int asc_maxHp = 80;
    private static final int preBattle_Block = 150;
    private static final int preBattle_ascBlock = 170;
    private static final int expel_attacks = 3;
    private static final int reboot_HP = 20;
    private boolean firstMove;
    private boolean secondMove;
    private boolean thirdMove;
    private boolean secondStage;
    private int expel_dmg;
    private int exterminate_dmg;
    private int destroy_dmg;
    private int ult_dmg;
    private int olb_dmg;
    private int nerf_debuff;
    private int act_block;
    private int upgrade_str;
    private int upgraded_times;
    private int obliterate_times;
    private int expel_times;
    private float fireTimer = 0F;
    private boolean extraOrbs = false;
    private ArrayList<CorruardianOrbsEffect> orbs = new ArrayList<>();
    
    public Corruardian(float x, float y) {
        super(NAME, ID, 70, 0.0F, 10.0F, 280.0F, 280.0F, null, x, y);
        this.firstMove = true;
        this.secondMove = true;
        this.thirdMove = true;
        this.secondStage = false;
        this.upgraded_times = 0;
        this.expel_times = 0;
        this.obliterate_times = 0;
        
        
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(asc_maxHp);
        } else {
            this.setHp(max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.expel_dmg = 6;
            this.exterminate_dmg = 25;
            this.destroy_dmg = 35;
            this.ult_dmg = 50;
            this.olb_dmg = 65;
        } else {
            this.expel_dmg = 5;
            this.exterminate_dmg = 20;
            this.destroy_dmg = 30;
            this.ult_dmg = 40;
            this.olb_dmg = 50;
        }
        this.damage.add(new DamageInfo(this, this.expel_dmg));
        this.damage.add(new DamageInfo(this, this.exterminate_dmg));
        this.damage.add(new DamageInfo(this, this.destroy_dmg));
        this.damage.add(new DamageInfo(this, this.ult_dmg));
        this.damage.add(new DamageInfo(this, this.olb_dmg));
        
        this.loadAnimation(CORAD_ATLAS, CORAD_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        this.stateData.setMix("Idle", "Attack", 0.1F);
        this.state.setTimeScale(0.8F);
        
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");
        UnlockTracker.markBossAsSeen(this.id);
        (AbstractDungeon.getCurrRoom()).cannotLose = true;
        this.addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
        this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, 2)));
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.addToBot(new GainBlockAction(this, this, preBattle_ascBlock));
        } else {
            this.addToBot(new GainBlockAction(this, this, preBattle_Block));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                if (AbstractDungeon.ascensionLevel >= 9) {
                    this.act_block = 50;
                } else {
                    this.act_block = 40;
                }
                this.addToBot(new ShoutAction(this, getActivateDialog(), 1.0F, 1.0F));
                this.addToBot(new GainBlockAction(this, this, this.act_block));
                this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, 2)));
                this.addToBot(new CorruardianSummonAction(this.hb_x, this.hb_y));
                if (MathUtils.randomBoolean()) {
                    this.addToBot(new SFXAction("SPHERE_DETECT_VO_1"));
                } else {
                    this.addToBot(new SFXAction("SPHERE_DETECT_VO_2"));
                }
                if (this.secondMove) {
                    this.secondMove = false;
                    this.setMove(MOVES[1], (byte)2, Intent.STRONG_DEBUFF);
                } else {
                    this.addToBot(new RollMoveAction(this));
                }
                break;
            case 2:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.nerf_debuff = 4;
                } else {
                    this.nerf_debuff = 2;
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.nerf_debuff, true), this.nerf_debuff));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.nerf_debuff, true), this.nerf_debuff));
                if (this.thirdMove) {
                    this.thirdMove = false;
                    this.setMove(MOVES[2], (byte)3, Intent.BUFF);
                } else {
                    this.addToBot(new RollMoveAction(this));
                }
                break;
            case 3:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.upgrade_str = 2;
                } else {
                    this.upgrade_str = 1;
                }
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, (this.upgrade_str + this.upgraded_times)), (this.upgrade_str + this.upgraded_times)));
                this.addToBot(new GainBlockAction(this, this, (6 + this.upgraded_times)));
                this.upgraded_times++;
                this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base, expel_attacks, true);
                break;
            case 4:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.4F));
                for (int i = 0; i < expel_attacks; i++) {
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                }
                this.expel_times++;
                if (this.expel_times == 2) {
                    this.setMove(MOVES[4], (byte)5, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                } else {
                    this.addToBot(new RollMoveAction(this));
                }
                break;
            case 5:    
                this.expel_times = 0;
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                int debuff = AbstractDungeon.aiRng.random(0, 1);
                if (debuff == 1) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, 1), 1));
                } else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new NoAttackPower(AbstractDungeon.player, 1, true), 1));
                }
                this.addToBot(new RollMoveAction(this));
                break;
            case 6:
                this.halfDead = false;
                this.extraOrbs = true;
                (AbstractDungeon.getCurrRoom()).cannotLose = false;
                this.addToBot(new ShoutAction(this, getRebootDialog(), 1.0F, 1.0F));
                this.increaseMaxHp(reboot_HP, true);
                this.addToBot(new HealAction(this, this, this.maxHealth, 0.1F));
                this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
                this.addToBot(new ApplyPowerAction(this, this, new CorruardianSelfDestructionPower(this), 1));
                if (noDestroyerExists()) {
                    this.addToBot(new CorruardianSummonAction(this.hb_x, this.hb_y));
                }
                
                if (MathUtils.randomBoolean()) {
                    this.addToBot(new SFXAction(TheManiac.makeID("CorruardianReboot_01"), 0.15F));
                } else {
                    this.addToBot(new SFXAction(TheManiac.makeID("CorruardianReboot_02"), 0.15F));
                }
                this.setMove(MOVES[6], (byte)7, Intent.ATTACK, this.damage.get(2).base);
                break;
            case 7:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.setMove(MOVES[7], (byte)8, Intent.ATTACK, this.damage.get(3).base);
                break;
            case 8:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.setMove(MOVES[8], (byte)9, Intent.ATTACK, this.damage.get(4).base);
                break;
            case 9:
                this.addToBot(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 10.0F * Settings.scale), 1.5F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(4), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.obliterate_times++;
                if (this.obliterate_times == 3) {
                    this.setMove(MOVES[9], (byte)10, Intent.UNKNOWN);
                } else {
                    this.setMove(MOVES[8], (byte)9, Intent.ATTACK, this.damage.get(4).base);
                }
                break;
            case 10:
                if (this.getPower(CorruardianSelfDestructionPower.POWER_ID) != null) {
                    ((CorruardianSelfDestructionPower)this.getPower(CorruardianSelfDestructionPower.POWER_ID)).orbs.hide();
                }
                this.addToBot(new VFXAction(new ExplosionSmallEffect(this.hb.cX, this.hb.cY), 0.1F));
                this.addToBot(new SuicideAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.maxHealth, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
                this.die(true);
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            this.setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
        } else {
            if (!this.lastMove((byte)4)) {
                this.setMove(MOVES[3], (byte)4, Intent.ATTACK, this.damage.get(0).base, expel_attacks, true);
            } else {
                if (playerHasNoDebuff()) {
                    this.setMove(MOVES[1], (byte)2, Intent.STRONG_DEBUFF);
                } else {
                    this.setMove(MOVES[2], (byte)3, Intent.BUFF);
                }
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.setTimeScale(0.8F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
        
        if (this.currentHealth <= 0 && !this.secondStage) {
            if ((AbstractDungeon.getCurrRoom()).cannotLose) {
                this.halfDead = true;
                this.secondStage = true;
            }
            for (AbstractPower p : this.powers) {
                p.onDeath();
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }
            this.addToTop(new ClearCardQueueAction());

            for (Iterator<AbstractPower> s = this.powers.iterator(); s.hasNext(); ) {
                AbstractPower p = s.next();
                if (p.type == AbstractPower.PowerType.DEBUFF || p.ID.equals("Shackled")) {
                    s.remove();
                }
            }
            
            this.setMove(MOVES[5], (byte)6, Intent.UNKNOWN);
            this.createIntent();
            this.addToBot(new ShoutAction(this, getRebootDialog()));
            this.addToBot(new SetMoveAction(this, (byte)3, Intent.UNKNOWN));
            applyPowers();
        }
    }

    

    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                if (this.getPower(CorruardianSelfDestructionPower.POWER_ID) != null) {
                    ((CorruardianSelfDestructionPower)this.getPower(CorruardianSelfDestructionPower.POWER_ID)).orbs.attackEffect();
                }
                this.state.setAnimation(0, "Attack", false);
                this.state.setTimeScale(0.8F);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            AbstractDungeon.scene.fadeInAmbiance();
            this.onBossVictoryLogic();

            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (!m.isDying && !m.isDead) {
                    this.addToBot(new HideHealthBarAction(m));
                    this.addToBot(new SuicideAction(m));
                    this.addToBot(new VFXAction(m, new InflameEffect(m), 0.2F));
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
    }

    private boolean noDestroyerExists() {
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m instanceof Destroyer) {
                if (!m.isDying) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean playerHasNoDebuff() {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                return false;
            }
        }
        
        return true;
    }
    
    private String getActivateDialog() {
        ArrayList<String> dialog = new ArrayList<>();
        dialog.add(DIALOG[0]);
        dialog.add(DIALOG[1]);
        dialog.add(DIALOG[2]);

        return dialog.get(AbstractDungeon.aiRng.random(0, dialog.size() - 1));
    }

    private String getRebootDialog() {
        ArrayList<String> dialog = new ArrayList<>();
        dialog.add(DIALOG[3]);
        dialog.add(DIALOG[4]);
        dialog.add(DIALOG[5]);

        return dialog.get(AbstractDungeon.aiRng.random(0, dialog.size() - 1));
    }
}
