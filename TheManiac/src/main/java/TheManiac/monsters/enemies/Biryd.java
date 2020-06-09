package TheManiac.monsters.enemies;

import TheManiac.powers.HoveringPower;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Biryd extends CustomMonster {
    public static final String ID = "maniac:Biryd";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String BIRYD_ATLAS = "maniacMod/images/monsters/enemies/Biryd/Biryd.atlas";
    private static final String BIRYD_JSON = "maniacMod/images/monsters/enemies/Biryd/Biryd.json";
    private static final int max_hp = 22;
    private static final int min_hp = 20;
    private static final int asc_maxHp = 26;
    private static final int asc_minHp = 24;
    private static final int peck_dmg = 2;
    private static final int claw_str = 1;
    private static final int claw_regen = 2;
    private int claw_block;
    private int fan_dmg;
    private int pecks;
    private int fan_debuff;
    
    public Biryd(float x, float y) {
        super(NAME, ID, 22, 20F, -6F, 240.0F, 180.0F, null, x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(asc_minHp, asc_maxHp);
        } else {
            this.setHp(min_hp, max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.claw_block = 6;
            this.fan_debuff = 3;
        } else {
            this.claw_block = 4;
            this.fan_debuff = 2;
        }
        
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.fan_dmg = 8;
        } else {
            this.fan_dmg = 7;
        }
        this.damage.add(new DamageInfo(this, peck_dmg));
        this.damage.add(new DamageInfo(this, fan_dmg));
        
        this.loadAnimation(BIRYD_ATLAS, BIRYD_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle_flap", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new HoveringPower(this)));
    }

    @Override
    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 1:
                this.addToBot(new AnimateFastAttackAction(this));
                if (pecks <= 3) {
                    pecks = 3;
                }
                
                for (i = 0; i < pecks; i++) {
                    playRandomBirdSFx();
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                }
                break;
            case 2:
                this.addToBot(new SFXAction("BYRD_DEATH"));
                this.addToBot(new TalkAction(this, DIALOG[0], 1.2F, 1.2F));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, claw_str), claw_str));
                this.addToBot(new GainBlockAction(this, claw_block));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    this.addToBot(new ApplyPowerAction(this, this, new RegenPower(this, claw_regen), claw_regen));
                }
                break;
            case 3:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, fan_debuff, true), fan_debuff));
                break;
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        pecks = this.currentHealth / 2;
        if (pecks <= 3) {
            pecks = 3;
        }
        
        if (num < 25) {
            if (!this.lastMove((byte)2)) {
                this.setMove((byte)2, Intent.BUFF);
            } else {
                if (AbstractDungeon.aiRng.randomBoolean(0.35F)) {
                    this.setMove(MOVES[0], (byte)3, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                } else {
                    this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, pecks, true);
                }
            }
        }
        else if (num < 55) {
            if (!this.lastMove((byte)1)) {
                this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, pecks, true);
            } else {
                if (AbstractDungeon.aiRng.randomBoolean(0.25F)) {
                    this.setMove((byte)2, Intent.BUFF);
                } else {
                    this.setMove(MOVES[0], (byte)3, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                }
            }
        } else {
            if (!this.lastTwoMoves((byte)3)) {
                this.setMove(MOVES[0], (byte)3, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
            } else {
                if (AbstractDungeon.aiRng.randomBoolean(0.45F)) {
                    this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, pecks, true);
                } else {
                    this.setMove((byte)2, Intent.BUFF);
                }
            }
        }
    }

    private void playRandomBirdSFx() { this.addToBot(new SFXAction("MONSTER_BYRD_ATTACK_" + MathUtils.random(0, 5))); }

    @Override
    public void die() {
        super.die();
        CardCrawlGame.sound.play("BYRD_DEATH");
    }
}
