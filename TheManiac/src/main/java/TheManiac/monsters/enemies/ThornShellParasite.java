package TheManiac.monsters.enemies;

import TheManiac.TheManiac;
import TheManiac.powers.WeaknessPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class ThornShellParasite extends CustomMonster {
    public static final String ID = "maniac:thornShellParasite";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String SHELL_ATLAS = "maniacMod/images/monsters/enemies/thornShellParasite/skeleton.atlas";
    private static final String SHELL_JSON = "maniacMod/images/monsters/enemies/thornShellParasite/skeleton.json";
    private static final int max_hp = 58;
    private static final int min_hp = 56;
    private static final int asc_maxHp = 64;
    private static final int asc_minHp = 60;
    private static final int thorn = 1;
    private int fellArmor;
    private int strike_weakness;
    private int strike_dmg;
    private int fell_dmg;
    private int suck_dmg;
    private boolean firstMove;
    public static final String ARMOR_BREAK = "ARMOR_BREAK";
    
    public ThornShellParasite(float x, float y) {
        super(NAME, ID, 58, 20F, -6F, 350F, 260F, null, x, y);
        this.firstMove = true;
        
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(asc_minHp, asc_maxHp);
        } else {
            this.setHp(min_hp, max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.fellArmor = 4;
        } else {
            this.fellArmor = 2;
        }
        
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (TheManiac.challengerMode) {
                this.strike_weakness = 4;
            } else {
                this.strike_weakness = 3;
            }
        } else {
            this.strike_weakness = 2;
        }
        
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.strike_dmg = 9;
            this.suck_dmg = 13;
            this.fell_dmg = 22;
        } else {
            this.strike_dmg = 7;
            this.suck_dmg = 10;
            this.fell_dmg = 20;
        }
        this.damage.add(new DamageInfo(this, this.strike_dmg));
        this.damage.add(new DamageInfo(this, this.suck_dmg));
        this.damage.add(new DamageInfo(this, this.fell_dmg));
        
        this.loadAnimation(SHELL_ATLAS, SHELL_JSON, 0.75F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        e.setTimeScale(0.8F);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, 16)));
        this.addToBot(new ApplyPowerAction(this, this, new ThornsPower(this, thorn)));
        this.addToBot(new GainBlockAction(this, this, 16));
    }

    @Override
    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 1:
                for (i = 0; i < 2; i++) {
                    this.addToBot(new AnimateHopAction(this));
                    this.addToBot(new WaitAction(0.2F));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeaknessPower(AbstractDungeon.player, strike_weakness), strike_weakness));
                }
                break;
            case 2:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.4F));
                this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-25.0F, 25.0F) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.RED.cpy()), 0.0F));
                this.addToBot(new VampireDamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
            case 3:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new WaitAction(0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, fellArmor), fellArmor));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    this.addToBot(new ApplyPowerAction(this, this, new ThornsPower(this, thorn), thorn));
                }
                break;
            case 4:
                this.addToBot(new ApplyPowerAction(this, this, new ThornsPower(this, thorn), thorn));
                break;
            case 5:
                if (AbstractDungeon.ascensionLevel >= 17) {
                    this.setMove((byte)3, Intent.ATTACK_BUFF, this.damage.get(2).base);
                } else {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
                }
                this.addToBot(new RollMoveAction(this));
                break;
        }

        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            if (AbstractDungeon.ascensionLevel >= 17) {
                if (AbstractDungeon.aiRng.randomBoolean(0.5F)) {
                    this.setMove((byte)4, Intent.BUFF);
                } else {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
                }
            } else {
                if (AbstractDungeon.aiRng.randomBoolean(0.25F)) {
                    this.setMove((byte)4, Intent.BUFF);
                } else {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
                }
            }
        } else {
            if (num < 25) {
                if (this.lastMove((byte)3)) {
                    if (AbstractDungeon.aiRng.randomBoolean(0.45F)) {
                        this.setMove(MOVES[0], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                    } else {
                        this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
                    }
                } else {
                    this.setMove((byte)3, Intent.ATTACK_BUFF, this.damage.get(2).base);
                }
            }
            else if (num < 55) {
                if (!this.lastTwoMoves((byte)2)) {
                    this.setMove(MOVES[0], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                } else {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
                }
            } else {
                if (!lastTwoMoves((byte)1)) {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
                } else {
                    if (AbstractDungeon.ascensionLevel >= 17) {
                        if (AbstractDungeon.aiRng.randomBoolean(0.35F)) {
                            this.setMove((byte)3, Intent.ATTACK_BUFF, this.damage.get(2).base);
                        } else {
                            this.setMove(MOVES[0], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
                        }
                    } else {
                        this.setMove(MOVES[0], (byte)2, Intent.ATTACK_BUFF, this.damage.get(1).base);
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
            case "ARMOR_BREAK":
                AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                setMove((byte)5, AbstractMonster.Intent.STUN);
                createIntent();
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
}
