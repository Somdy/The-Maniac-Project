package TheManiac.monsters.enemies;

import TheManiac.actions.MakeRandomTypeCardInPileAction;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.GenericStrengthUpPower;

public class Kaleidoscrimson extends CustomMonster {
    public static final String ID = "maniac:Kaleidoscrimson";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String KAL_ATLAS = "maniacMod/images/monsters/enemies/Kaleidoscrimson/Kaleidoscrimson.atlas";
    private static final String KAL_JSON = "maniacMod/images/monsters/enemies/Kaleidoscrimson/skeleton.json";
    private static final int max_hp = 68;
    private static final int min_hp = 64;
    private static final int asc_maxHp = 78;
    private static final int asc_minHp = 74;
    private static final int str_power = 2;
    private static final int asc_str = 4;
    private int laser_dmg;
    private int beam_dmg;
    private int claw_dmg;
    private int tempCard;
    
    public Kaleidoscrimson(float x, float y) {
        super(NAME, ID, 74, -20F, -14F, 280F, 250F, null, x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(asc_minHp, asc_maxHp);
        } else {
            this.setHp(min_hp, max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.tempCard = 2;
        } else {
            this.tempCard = 1;
        }
        
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.laser_dmg = 9;
            this.beam_dmg = 11;
            this.claw_dmg = 13;
        } else {
            this.laser_dmg = 8;
            this.beam_dmg = 10;
            this.claw_dmg = 12;
        }
        this.damage.add(new DamageInfo(this, this.laser_dmg));
        this.damage.add(new DamageInfo(this, this.beam_dmg));
        this.damage.add(new DamageInfo(this, this.claw_dmg));
        
        this.loadAnimation(KAL_ATLAS, KAL_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.addToBot(new ApplyPowerAction(this, this, new GenericStrengthUpPower(this, MOVES[0], asc_str)));
        } else {
            this.addToBot(new ApplyPowerAction(this, this, new GenericStrengthUpPower(this, MOVES[0], str_power)));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.4F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                this.addToBot(new MakeRandomTypeCardInPileAction(AbstractDungeon.player, AbstractCard.CardType.STATUS, AbstractDungeon.player.drawPile, tempCard));
                break;
            case 2:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.4F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.LIGHTNING));
                this.addToBot(new MakeRandomTypeCardInPileAction(AbstractDungeon.player, AbstractCard.CardType.CURSE, AbstractDungeon.player.discardPile, tempCard));
                break;
            case 3:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (num < 35) {
                if (!this.lastTwoMoves((byte)2)) {
                    this.setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                }
                else if (!this.lastMove((byte)1)) {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                } else {
                    this.setMove((byte)3, Intent.ATTACK, this.damage.get(2).base);
                }
            }
            else if (num < 40) {
                if (!this.lastTwoMoves((byte)1)) {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                } else {
                    if (AbstractDungeon.aiRng.randomBoolean(0.55F)) {
                        this.setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                    } else {
                        this.setMove((byte)3, Intent.ATTACK, this.damage.get(2).base);
                    }
                }
            } else {
                if (!this.lastMove((byte)3)) {
                    this.setMove((byte)3, Intent.ATTACK, this.damage.get(2).base);
                } else {
                    if (AbstractDungeon.aiRng.randomBoolean(0.40F)) {
                        this.setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                    } else {
                        this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                    }
                }
            }
        } else {
            if (num < 20) {
                if (!this.lastTwoMoves((byte)2)) {
                    this.setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                } else {
                    if (AbstractDungeon.aiRng.randomBoolean(0.40F)) {
                        this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                    } else {
                        this.setMove((byte)3, Intent.ATTACK, this.damage.get(2).base);
                    }
                }
            }
            else if (num < 30) {
                if (!this.lastTwoMoves((byte)1)) {
                    this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                } else {
                    if (AbstractDungeon.aiRng.randomBoolean(0.25F)) {
                        this.setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                    } else {
                        this.setMove((byte)3, Intent.ATTACK, this.damage.get(2).base);
                    }
                }
            } else {
                if (!this.lastMove((byte)3)) {
                    this.setMove((byte)3, Intent.ATTACK, this.damage.get(2).base);
                } else {
                    if (AbstractDungeon.aiRng.randomBoolean(0.35F)) {
                        this.setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                    } else {
                        this.setMove((byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
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
