package TheManiac.monsters.enemies;

import TheManiac.actions.MawellingUpgradeMudAction;
import TheManiac.cards.status.TheUndigested;
import TheManiac.powers.DigestPower;
import TheManiac.powers.SalivaPower;
import TheManiac.powers.SwellingPower;
import TheManiac.vfx.FlashManiacAtkEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Mawelling extends CustomMonster {
    public static final String ID = "maniac:Mawelling";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String MAW_ATLAS = "maniacMod/images/monsters/enemies/infectiousMaw/skeleton.atlas";
    private static final String MAW_JSON = "maniacMod/images/monsters/enemies/infectiousMaw/skeleton.json";
    private static final int max_hp = 230;
    private static final int asc_maxHp = 240;
    private int mud_count;
    private int drool_swelling;
    private int drool_str;
    private int vomit_swelling;
    private int spittle_debuff;
    private static final int lick_dmg = 10;
    private int lick_debuff;
    private int chew_count;
    private boolean firstMove;
    private boolean initialDrool;
    private boolean initialLick;
    private boolean firstRum;
    private boolean secondRum;
    public int totalSwelling;

    private static final Logger logger = LogManager.getLogger(Mawelling.class.getName());
    
    public Mawelling(float x, float y) {
        super(NAME, ID, 240, 0.0F, -40.0F, 430.0F, 360.0F, null, x, y);
        this.type = EnemyType.BOSS;
        this.firstMove = true;
        this.initialDrool = true;
        this.initialLick = true;
        this.chew_count = 0;
        this.totalSwelling = 0;
        this.firstRum = true;
        this.secondRum = AbstractDungeon.ascensionLevel >= 19;
        this.drool_str = 1;
        
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(max_hp);
        } else {
            this.setHp(asc_maxHp);
        }
        
        this.damage.add(new DamageInfo(this, lick_dmg));
        this.damage.add(new DamageInfo(this, -1));
        this.damage.add(new DamageInfo(this, -1));
        logger.info("Lick damage: " + this.damage.get(0) + " Static lick damage: " + lick_dmg);
        
        this.loadAnimation(MAW_ATLAS, MAW_JSON, 0.85F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.dialogX = -160.0F * Settings.scale;
        this.dialogY = 40.0F * Settings.scale;
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BOTTOM");
        }
        
        this.addToTop(new ApplyPowerAction(this, this, new DigestPower(this, 0), 0));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                int HpIncrease = AbstractDungeon.player.maxHealth / 2;
                if (HpIncrease < 20) {
                    HpIncrease = 20;
                }
                HpIncrease *= AbstractDungeon.ascensionLevel >= 19 ? 3 : 2.5;
                float IncreasePer = (HpIncrease / 100F) + 0.05F;
                this.addToBot(new SFXAction("MAW_DEATH", 0.1F));
                this.addToBot(new ShoutAction(this, getDevourDialog(), 1.0F, 2.0F));
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new VFXAction(new BorderFlashEffect(Color.SALMON)));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale,
                        AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.SALMON.cpy()), 1.5F));
                AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth / 2);
                this.addToBot(new IncreaseMaxHpAction(this, IncreasePer, true));
                this.addToBot(new HealAction(this, this, this.maxHealth));
                this.setMove(MOVES[1], (byte)2, Intent.STRONG_DEBUFF);
                break;
            case 2:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.mud_count = 6;
                } else {
                    this.mud_count = 4;
                }
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new SFXAction("MONSTER_SLIME_ATTACK", 0.2F));
                this.addToBot(new MakeTempCardInDiscardAction(new TheUndigested(), this.mud_count));
                if (this.initialDrool) {
                    this.setMove(MOVES[2], (byte)3, Intent.BUFF);
                    this.createIntent();
                } else {
                    this.addToBot(new RollMoveAction(this));
                }
                break;
            case 3:
                this.initialDrool = false;
                if (AbstractDungeon.ascensionLevel >= 9) {
                    this.drool_swelling = 35;
                } else {
                    this.drool_swelling = 25;
                }
                this.addToBot(new VFXAction(this, new InflameEffect(this), 0.5F));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.drool_str), this.drool_str));
                this.addToBot(new ApplyPowerAction(this, this, new SwellingPower(this, this.drool_swelling), this.drool_swelling));
                if (this.initialLick) {
                    this.setMove(MOVES[3], (byte)4, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                    this.createIntent();
                } else {
                    this.addToBot(new RollMoveAction(this));
                }
                break;
            case 4:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.lick_debuff = 10;
                } else {
                    this.lick_debuff = 6;
                }
                this.initialLick = false;
                this.addToBot(new AnimateSlowAttackAction(this));
                AbstractDungeon.effectList.add(new FlashManiacAtkEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "MANIAC_BLUNT", false));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new SalivaPower(this, AbstractDungeon.player, this.lick_debuff), this.lick_debuff));
                this.addToBot(new FastShakeAction(AbstractDungeon.player, 0.5F, 0.5F));
                this.addToBot(new RollMoveAction(this));
                break;
            case 5:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.spittle_debuff = 3;
                } else {
                    this.spittle_debuff = 2;
                }
                int debuff = AbstractDungeon.aiRng.random(0, 1);
                if (debuff == 1) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.spittle_debuff, true), this.spittle_debuff));
                } else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.spittle_debuff, true), this.spittle_debuff));
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new SalivaPower(this, AbstractDungeon.player, 6), 6));
                this.addToBot(new MawellingUpgradeMudAction());
                this.addToBot(new RollMoveAction(this));
                break;
            case 6:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new SFXAction("MAW_DEATH", 0.1F));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale,
                        AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.CORAL.cpy()), 0.3F));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale,
                        AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.BROWN.cpy()), 0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.chew_count++;
                if (this.chew_count == 2) {
                    this.FeastSet();
                } else {
                    this.addToBot(new RollMoveAction(this));
                }
                break;
            case 7:
                this.chew_count = 0;
                this.addToBot(new ShoutAction(this, getFeastDialog(), 1.0F, 2.0F));
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new SFXAction("VO_SLIMEBOSS_1A", 0.2F));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale,
                        AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 50.0F) * Settings.scale, Color.YELLOW.cpy()), 0.3F));
                this.addToBot(new VampireDamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                this.addToBot(new RollMoveAction(this));
                break;
            case 8:
                this.RuminateAction();
                this.addToBot(new RollMoveAction(this));
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            this.setMove(MOVES[0], (byte)1, Intent.STRONG_DEBUFF);
        } else {
            if (this.currentHealth < this.maxHealth / 2 && this.firstRum) {
                this.firstRum = false;
                this.setMove(MOVES[7], (byte)8, Intent.BUFF);
            }
            else if (this.currentHealth < this.maxHealth / 4 && this.secondRum) {
                this.secondRum = false;
                this.setMove(MOVES[7], (byte)8, Intent.BUFF);
            }
            
            if (num < 35) {
                if (this.lastMoveBefore((byte)3)) {
                    if (AbstractDungeon.aiRng.randomBoolean(0.45F)) {
                        this.setMove(MOVES[3], (byte)4, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                    } else {
                        this.setMove(MOVES[4], (byte)5, Intent.DEBUFF);
                    }
                } else {
                    this.setMove(MOVES[2], (byte)3, Intent.BUFF);
                }
            }
            else if (num < 55F) {
                if (this.lastMoveBefore((byte)4)) {
                    if (AbstractDungeon.aiRng.randomBoolean(0.33F)) {
                        this.setMove(MOVES[2], (byte)3, Intent.BUFF);
                    } else {
                        ChewUpSet();
                    }
                } else {
                    this.setMove(MOVES[3], (byte)4, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                }
            } else {
                if (!this.lastMove((byte)6)) {
                    ChewUpSet();
                } else {
                    this.setMove(MOVES[3], (byte)4, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                }
            }
        }
    }
    
    private void FeastSet() {
        int mudCount = 0;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof TheUndigested) {
                mudCount++;
            }
        }
        
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof TheUndigested) {
                mudCount++;
            }
        }
        
        if (mudCount <= 0) {
            mudCount = 10;
        }
        logger.info("The Undigested counts: " + mudCount);
        this.damage.get(2).base = mudCount;
        this.setMove(MOVES[6], (byte)7, Intent.ATTACK_BUFF, this.damage.get(2).base);
        this.createIntent();
    }
    
    private void ChewUpSet() {
        int DMG;
        if (this.getPower(DigestPower.POWER_ID) != null) {
            DMG = (this.getPower(DigestPower.POWER_ID)).amount;
        } else {
            DMG = AbstractDungeon.aiRng.random(12, 16);
        }
        
        if (DMG <= 5) {
            DMG = 5;
        }
        
        DMG *= 1.5F;
        this.damage.get(1).base = DMG;
        this.setMove(MOVES[5], (byte)6, Intent.ATTACK, this.damage.get(1).base);
        this.createIntent();
    }
    
    private void RuminateAction() {
        if (this.totalSwelling > 0) {
            logger.info("Total Swelling gained this combat: " + totalSwelling);
            this.addToBot(new HealAction(this, this, this.totalSwelling));
        } else {
            this.addToBot(new HealAction(this, this, (this.maxHealth / 4)));
        }
    }
    
    private String getDevourDialog() {
        ArrayList<String> dialog = new ArrayList<>();
        dialog.add(DIALOG[0]);
        dialog.add(DIALOG[1]);
        dialog.add(DIALOG[2]);
        
        return dialog.get(AbstractDungeon.aiRng.random(0, dialog.size() - 1));
    }

    private String getFeastDialog() {
        ArrayList<String> dialog = new ArrayList<>();
        dialog.add(DIALOG[4]);
        dialog.add(DIALOG[5]);
        dialog.add(DIALOG[6]);
        
        return dialog.get(AbstractDungeon.aiRng.random(0, dialog.size() - 1));
    }

    @Override
    public void die() {
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        CardCrawlGame.sound.play("MAW_DEATH");
        AbstractDungeon.scene.fadeInAmbiance();
        this.onBossVictoryLogic();
    }
}
