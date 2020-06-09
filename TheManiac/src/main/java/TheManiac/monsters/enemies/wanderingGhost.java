package TheManiac.monsters.enemies;

import TheManiac.powers.GhostImmunePower;
import TheManiac.vfx.BetterScreenOnFireEffect;
import TheManiac.vfx.GhostFireBallEffect;
import TheManiac.vfx.GhostIgniteFireEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.BurnIncreaseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

import java.util.ArrayList;

public class wanderingGhost extends CustomMonster {
    public static final String ID = "maniac:wanderingGhost";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String CORE_IMG = "maniacMod/images/monsters/enemies/wanderingGhost/core.png";
    private static final int max_hp = 104;
    private static final int min_hp = 100;
    private static final int asc_maxHp = 110;
    private static final int asc_minHp = 106;
    private int sear_burn;
    private int sear_dmg;
    private int inflame_str;
    private static final int divide_attacks = 6;
    private static final int sear_attacks = 2;
    private ArrayList<wanderingGhostOrb> orbs = new ArrayList<>();
    private AbstractMonster[] torches = new AbstractMonster[2];
    private wanderingGhostBody body;
    private float spawnX = -245.0F;
    private boolean activated;
    private boolean firstMove;
    private int sear_count;
    private int inflame_count;
    private int divide_count;
    
    
    public wanderingGhost() {
        super(NAME, ID, 114, 20.0F, 0.0F, 450.0F, 450.0F, CORE_IMG);
        this.type = EnemyType.ELITE;
        this.body = new wanderingGhostBody(this);
        this.disposables.add(this.body);
        this.firstMove = true;
        this.activated = false;
        this.sear_count = this.inflame_count = this.divide_count = 0;
        
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.setHp(asc_minHp, asc_maxHp);
            this.inflame_str = 2;
            this.sear_burn = 2;
        } else {
            this.setHp(min_hp, max_hp);
            this.inflame_str = 1;
            this.sear_burn = 1;
        }
        
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.sear_dmg = 2;
        } else {
            this.sear_dmg = 1;
        }
        this.damage.add(new DamageInfo(this, this.sear_dmg));
        this.damage.add(new DamageInfo(this, -1));
        this.damage.add(new DamageInfo(this, -1));

        this.orbs.add(new wanderingGhostOrb(-90.0F, 380.0F, this.orbs.size()));
        this.orbs.add(new wanderingGhostOrb(90.0F, 380.0F, this.orbs.size()));
        this.orbs.add(new wanderingGhostOrb(160.0F, 250.0F, this.orbs.size()));
        this.orbs.add(new wanderingGhostOrb(90.0F, 120.0F, this.orbs.size()));
        this.orbs.add(new wanderingGhostOrb(-90.0F, 120.0F, this.orbs.size()));
        this.orbs.add(new wanderingGhostOrb(-160.0F, 250.0F, this.orbs.size()));
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new GhostImmunePower(this)));
    }
    
    

    @Override
    public void takeTurn() {
        int DMG;
        int HELL_DMG;
        switch (this.nextMove) {
            case 5:
                this.addToBot(new ChangeStateAction(this, "Activate"));
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        if (AbstractDungeon.ascensionLevel >= 18) {
                            this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                        }
                        continue;
                    }
                    else if (!m.isDying) {
                        this.addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, 1), 1));
                    }
                }
                
                if (!AbstractDungeon.player.drawPile.isEmpty() || !AbstractDungeon.player.discardPile.isEmpty()) {
                    DMG = Math.abs(AbstractDungeon.player.drawPile.group.size() - AbstractDungeon.player.discardPile.group.size());
                } else {
                    DMG = Math.abs(AbstractDungeon.player.masterDeck.group.size() - AbstractDungeon.player.exhaustPile.group.size());
                }
                
                if (DMG < 1) {
                    DMG = 1;
                } else if (DMG * divide_attacks >= AbstractDungeon.player.maxHealth) {
                    DMG = AbstractDungeon.player.maxHealth / 7;
                }
                
                this.damage.get(1).base = DMG;
                applyPowers();
                this.setMove(MOVES[0], (byte)1, Intent.ATTACK, this.damage.get(1).base, divide_attacks, true);
                break;
            case 1:
                this.divide_count++;
                this.sear_count = this.inflame_count = 0;
                for (int i = 0; i < divide_attacks; i++) {
                    this.addToBot(new VFXAction(new BorderFlashEffect(Color.ROYAL)));
                    this.addToBot(new VFXAction(this, new GhostIgniteFireEffect(
                            AbstractDungeon.player.hb.cX + MathUtils.random(-120.0F, 120.0F) * Settings.scale, 
                            AbstractDungeon.player.hb.cY + MathUtils.random(-120.0F, 120.0F) * Settings.scale), 0.05F));
                    this.addToBot(new SFXAction("GHOST_ORB_IGNITE_1", 0.3F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                }
                
                if (this.divide_count != 3) {
                    this.addToBot(new RollMoveAction(this));
                } else {
                    HELL_DMG = AbstractDungeon.player.drawPile.group.size() + AbstractDungeon.player.discardPile.group.size();
                    if (HELL_DMG <= 0) {
                        HELL_DMG = AbstractDungeon.player.exhaustPile.group.size();
                    }
                    
                    this.damage.get(2).base = HELL_DMG;
                    
                    this.setMove(MOVES[3], (byte)6, Intent.ATTACK, this.damage.get(2).base, divide_attacks, true);
                }
                break;
            case 2:
                this.inflame_count++;
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        this.addToBot(new VFXAction(this, new InflameEffect(this), 0.5F));
                        this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.inflame_str), this.inflame_str));
                        continue;
                    }
                    else if (!m.isDying) {
                        this.addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, this.inflame_str), this.inflame_str));
                        this.addToBot(new GainBlockAction(m, this, 6));
                    }
                }
                this.addToBot(new RollMoveAction(this));
                break;
            case 3:
                this.sear_count++;
                for (int i = 0; i < sear_attacks; i++) {
                    this.addToBot(new VFXAction(this, new GhostFireBallEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                    this.addToBot(new VFXAction(this, new GhostIgniteFireEffect(
                            AbstractDungeon.player.hb.cX + MathUtils.random(-120.0F, 120.0F) * Settings.scale,
                            AbstractDungeon.player.hb.cY + MathUtils.random(-120.0F, 120.0F) * Settings.scale), 0.05F));
                    this.addToBot(new SFXAction("GHOST_ORB_IGNITE_2", 0.3F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                }
                Burn burn = new Burn();
                this.addToBot(new MakeTempCardInDiscardAction(burn.makeCopy(), this.sear_burn));
                this.addToBot(new RollMoveAction(this));
                break;
            case 4:
                for (int i = 0; i < 2; i++) {
                    AbstractMonster m = new TorchGhost(this.spawnX + -215.0F * i, MathUtils.random(-5.0F, 5.0F));
                    this.addToBot(new SpawnMonsterAction(m, false));
                }
                this.setMove(MOVES[1], (byte)2, Intent.BUFF);
                break;
            case 6:
                this.divide_count = 0;
                this.addToBot(new VFXAction(this, new BetterScreenOnFireEffect(Color.VIOLET), 1F));
                for (int i = 0; i< divide_attacks; i++) {
                    this.addToBot(new VFXAction(this, new GhostIgniteFireEffect(
                            AbstractDungeon.player.hb.cX + MathUtils.random(-120.0F, 120.0F) * Settings.scale,
                            AbstractDungeon.player.hb.cY + MathUtils.random(-120.0F, 120.0F) * Settings.scale), 0.05F));
                    this.addToBot(new SFXAction("GHOST_ORB_IGNITE_1", 0.3F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.FIRE, true));
                }
                this.addToBot(new BurnIncreaseAction());
                System.out.println(this.name + "says: Unbelievable! You should suffer a Hellfire!");
                this.addToBot(new RollMoveAction(this));
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            this.setMove((byte)5, Intent.UNKNOWN);
        } else {
            if (numTorches() >= 1) {
                if (this.sear_count >= 2 && this.inflame_count >= 2) {
                    this.setMove(MOVES[0], (byte)1, Intent.ATTACK, this.damage.get(1).base, divide_attacks, true);
                } else {
                    if (num < 45) {
                        if (!this.lastMove((byte)2)) {
                            this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                        } else {
                            this.setMove(MOVES[2], (byte)3, Intent.ATTACK_DEBUFF, this.damage.get(0).base, sear_attacks, true);
                        }
                    } else {
                        if (!this.lastTwoMoves((byte)3)) {
                            this.setMove(MOVES[2], (byte)3, Intent.ATTACK_DEBUFF, this.damage.get(0).base, sear_attacks, true);
                        } else {
                            this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                        }
                    }
                }
            } else {
                this.setMove((byte)4, Intent.UNKNOWN);
            }
        }
    }
    
    private int numTorches() {
        int count = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m != null && m != this && !m.isDying) {
                count++;
            }
        }
        
        return count;
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "Activate":
                for (wanderingGhostOrb orb: this.orbs) {
                    if (!orb.isActivated) {
                        orb.activate(this.drawX + this.animX, this.drawY + this.animY);
                    }
                }
                this.body.targetRotSpeed = 120F;
                break;
            default:   
        }
    }

    @Override
    public void die() {
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4F);
        super.die();
        for (wanderingGhostOrb orb : this.orbs) {
            orb.hide();
        }
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDying && !m.isDead) {
                this.addToBot(new HideHealthBarAction(m));
                this.addToBot(new SuicideAction(m));
                this.addToBot(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
        }
    }

    @Override
    public void update() {
        super.update();
        this.body.update();
        for (wanderingGhostOrb orb : this.orbs) {
            orb.update(this.drawX + this.animX, this.drawY + this.animY);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.body.render(sb);
        super.render(sb);
    }
}
