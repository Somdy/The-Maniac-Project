package TheManiac.monsters.enemies;

import TheManiac.TheManiac;
import TheManiac.powers.CursePower;
import TheManiac.powers.WeaknessPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class Possessed extends CustomMonster {
    public static final String ID = "maniac:Possessed";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String POSED_ATLAS = "maniacMod/images/monsters/enemies/Possessed/Possessed.atlas";
    private static final String POSED_JSON = "maniacMod/images/monsters/enemies/Possessed/skeleton.json";
    private static final int max_hp = 106;
    private static final int min_hp = 102;
    private static final int asc_maxHp = 110;
    private static final int asc_minHp = 106;
    private static final int poke_attaks = 3;
    private static final int drain_str = 2;
    private static final int drain_debuff = -1;
    private static final int delibilitate = 2;
    private static final int frenzy_str = 2;
    private static final int frenzy_block = 10;
    private static final int zap_debuff = 4;
    private boolean firstMove;
    private boolean usedCurse;
    private int poke_dmg;
    private int zap_dmg;
    private int frenzy_times;
    
    public Possessed(float x, float y) {
        super(NAME, ID, 90, 5.0F, -10.0F, 200.0F, 280.0F, null, x, y - 20F);
        this.firstMove = true;
        this.usedCurse = false;
        this.frenzy_times = 0;
        
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(asc_minHp, asc_maxHp);
        } else {
            this.setHp(min_hp, max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.poke_dmg = 4;
            this.zap_dmg = 9;
        } else {
            this.poke_dmg = 3;
            this.zap_dmg = 8;
        }
        this.damage.add(new DamageInfo(this, this.poke_dmg));
        this.damage.add(new DamageInfo(this, this.zap_dmg));
        
        this.loadAnimation(POSED_ATLAS, POSED_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        this.stateData.setMix("Attack", "Idle", 0.2F);
        this.state.setTimeScale(0.8F);
        
        this.dialogX = -30.0F * Settings.scale;
        this.dialogY = 50.0F * Settings.scale;
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                this.addToBot(new FastShakeAction(this, 0.3F, 0.5F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 2:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY), 0.1F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeaknessPower(AbstractDungeon.player, (zap_debuff + this.frenzy_times)), (zap_debuff + this.frenzy_times)));
                break;
            case 3:
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, delibilitate, true), delibilitate));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, delibilitate, true), delibilitate));
                break;
            case 4:
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, drain_debuff), drain_debuff));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, drain_str), drain_str));
                break;
            case 5:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new ShoutAction(this, DIALOG[0], 1F, 1F));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new CursePower(AbstractDungeon.player)));
                break;
            case 6:
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, frenzy_str), frenzy_str));
                        this.addToBot(new GainBlockAction(this, this, frenzy_block));
                    }
                    else if (!m.isDying) {
                        this.addToBot(new ApplyPowerAction(m, this, new StrengthPower(this, frenzy_str), frenzy_str));
                        this.addToBot(new GainBlockAction(m, this, frenzy_block));
                    }
                }
                this.frenzy_times++;
                break;
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (!this.usedCurse) {
                this.usedCurse = true;
                this.setMove(MOVES[0], (byte)5, Intent.STRONG_DEBUFF);
            } 
            else if (!this.lastMove((byte)2) && !this.lastMove((byte)3)) {
                if (num < 55) {
                    this.setMove(MOVES[1], (byte)2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                } else {
                    this.setMove(MOVES[2], (byte)3, Intent.DEBUFF);
                }
            }
            else if (num < 45) {
                this.setMove(MOVES[3], (byte)6, Intent.DEFEND_BUFF);
            } else {
                this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, poke_attaks, true);
            }
        }
        else if (this.firstMove) {
            this.firstMove = false;
            this.setMove(MOVES[1], (byte)2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
        else if (!this.usedCurse) {
            this.usedCurse = true;
            this.setMove(MOVES[0], (byte)5, Intent.STRONG_DEBUFF);
        }
        else if (!this.lastMove((byte)2) && !this.lastMove((byte)3)) {
            if (num < 55) {
                this.setMove(MOVES[1], (byte)2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
            } else {
                this.setMove(MOVES[2], (byte)3, Intent.DEBUFF);
            }
        }
        else if (num < 45) {
            this.setMove(MOVES[3], (byte)4, Intent.DEBUFF);
        } else {
            this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, poke_attaks, true);
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

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.setTimeScale(0.8F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void die() {
        super.die();
        CardCrawlGame.sound.play(TheManiac.makeID("ThePossessedDeath"));
    }
}
