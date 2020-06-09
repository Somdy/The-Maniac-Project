package TheManiac.monsters.minions;

import TheManiac.TheManiac;
import TheManiac.actions.MinionsUnique.RollNextMinionMove;
import TheManiac.minions.AbstractManiacMinion;
import TheManiac.powers.BleedingPower;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class StabbingBook extends AbstractManiacMinion {
    public static final String ID = TheManiac.makeID("StabbingBook");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String BOOK_ATLAS = "maniacMod/images/monsters/minions/StabbingBook/StabbingBook.atlas";
    private static final String BOOK_JSON = "maniacMod/images/monsters/minions/StabbingBook/skeleton.json";
    private int impaleDmg;
    private int lancinateDmg;
    private int bleeding;
    private int str_powers;
    
    public StabbingBook(int maxHp, int minHp, int baseDmg, float x, float y) {
        super(NAME, ID, maxHp, minHp, baseDmg, 0.0F, -10.0F, 220.0F, 320.0F, null, x, y);
        this.type = Enums.MINION_AGGRESSIVE;
        this.bleeding = 2;
        this.str_powers = 1;
        this.loadAnimation(BOOK_ATLAS, BOOK_JSON, 1.25F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        e.setTimeScale(0.8F);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
    }

    @Override
    public void initializeDmg(int baseDmg) {
        this.impaleDmg = baseDmg;
        this.lancinateDmg = (int) Math.ceil(baseDmg / 2D);
        this.damage.add(new DamageInfo(this, impaleDmg));
        this.damage.add(new DamageInfo(this, lancinateDmg));
    }

    @Override
    public void takeMinionTurn() {
        logger.info(this.name + "将要进行行动：" + this.nextMove);
        switch (this.nextMove) {
            case 1:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new SFXAction("MONSTER_BOOK_STAB_" + MathUtils.random(1, 4)));
                for (int i = 0; i < 5; i++) {
                    this.addToBot(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true, true));
                }
                break;
            case 2:
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new SFXAction("MONSTER_BOOK_STAB_" + MathUtils.random(0, 3)));
                for (int i = 0; i < 3; i++) {
                    this.addToBot(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true, true));
                    this.addToBot(new ApplyPowerAction(this.target, this, new BleedingPower(this.target, this.bleeding), this.bleeding));
                }
                break;
            case 3:
                this.addToBot(new VFXAction(new InflameEffect(this), 0.05F));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.str_powers), this.str_powers));
                break;
        }
        
        this.addToBot(new RollNextMinionMove(this));
    }

    @Override
    public void forceNextMove(boolean noValidTarget) {
        if (!noValidTarget) {
            AbstractMonster monster = (AbstractMonster) getValidTarget(true, false);

            logger.info("未知错误，强制采取" + this.name + "的下一步行动。");

            this.setMinionMove(MOVES[0], (byte)2, Intent.ATTACK_DEBUFF, monster, this.damage.get(1), 3, true);
            this.createIntent();
            this.applyPowers();
        } else {
            logger.info("矢野不提供可选目标，强制采取" + this.name + "的下一步行动。");
            this.setMinionMove((byte)3, Intent.BUFF, this);
            this.createIntent();
        }
    }

    @Override
    protected void getMove(int num) {
        AbstractMonster monster = (AbstractMonster) getValidTarget(true, false);
        
        if (num < 25) {
            if (!this.lastMove((byte)3) && !this.lastMoveBefore((byte)3)) {
                this.setMinionMove((byte)3, Intent.BUFF, this);
            } else {
                if (AbstractDungeon.aiRng.randomBoolean()) {
                    this.setMinionMove((byte)1, Intent.ATTACK, monster, this.damage.get(0), 5, true);
                } else {
                    this.setMinionMove(MOVES[0], (byte)2, Intent.ATTACK_DEBUFF, monster, this.damage.get(1), 3, true);
                }
            }
        }
        else if (num < 65) {
            if (!this.lastMove((byte)1) || this.lastMove((byte)3)) {
                this.setMinionMove((byte)1, Intent.ATTACK, monster, this.damage.get(0), 5, true);
            } else {
                if (!this.lastTwoMoves((byte)2)) {
                    this.setMinionMove(MOVES[0], (byte)2, Intent.ATTACK_DEBUFF, monster, this.damage.get(1), 3, true);
                } else {
                    this.setMinionMove((byte)3, Intent.BUFF, this);
                }
            }
        } else {
            if (!this.lastMoveBefore((byte)2) && !this.lastMove((byte)2)) {
                this.setMinionMove(MOVES[0], (byte)2, Intent.ATTACK_DEBUFF, monster, this.damage.get(1), 3, true);
            } else {
                if (AbstractDungeon.aiRng.randomBoolean()) {
                    this.setMinionMove((byte)3, Intent.BUFF, this);
                } else {
                    this.setMinionMove((byte)1, Intent.ATTACK, monster, this.damage.get(0), 5, true);
                }
            }
        }
    }

    @Override
    public void changeState(String stateName) {
        super.changeState(stateName);
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
            case "ATTACK_2":
                this.state.setAnimation(0, "Attack_2", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
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

    @Override
    public void die() {
        super.die();
        CardCrawlGame.sound.play("STAB_BOOK_DEATH");
    }
}