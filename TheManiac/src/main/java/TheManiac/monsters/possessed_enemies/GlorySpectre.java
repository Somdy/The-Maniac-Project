package TheManiac.monsters.possessed_enemies;

import TheManiac.TheManiac;
import TheManiac.actions.PossessedMonsterAction.SpectreSearAction;
import TheManiac.actions.PossessedMonsterAction.VanishAction;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import TheManiac.helper.ManiacImageMaster;
import TheManiac.powers.WeaknessPower;
import TheManiac.vfx.PossessedVfx.SpectreFireBallEffect;
import TheManiac.vfx.PossessedVfx.SpectreIgniteFireEffect;
import TheManiac.vfx.PossessedVfx.SpectreInflameFireEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class GlorySpectre extends AbstractPossessedMonster {
    public static final String ID = TheManiac.makeID("GlorySpectre");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String SPEC_ATLAS = "maniacMod/images/monsters/possessed_enemies/Spectres/gold/skeleton.atlas";
    private static final String SPEC_JSON = "maniacMod/images/monsters/possessed_enemies/Spectres/gold/skeleton.json";
    private static final int sear_count = 3;
    private static final int overflow_count = 6;
    private int searDmg;
    private int turnDmg;
    private int overflowDmg;
    private int inflame_str;
    private int inflame_block;
    private int weakness_amt;
    private boolean firstInflame;
    private boolean isSummoned;
    private boolean firstMove;
    private float fire_timer = 0F;
    private int vanishTurn;
    
    public GlorySpectre(int minHp, int maxHp, int baseDmg, int vanishTurn, float x, float y) {
        super(NAME, ID, PossessedClass.Glorious, minHp, maxHp, baseDmg, -8F, 6F, 240.0F, 260.0F, x, y);
        this.firstInflame = true;
        this.isSummoned = false;
        this.firstMove = true;
        this.vanishTurn = vanishTurn;
        this.inflame_str = ascLevel() > 17 ? 3 : 2;
        this.inflame_block = ascLevel() > 17 ? 20 : 14;
        this.weakness_amt = ascLevel() > 17 ? 6 : 5;
        this.loadAnimation(SPEC_ATLAS, SPEC_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public GlorySpectre(int minHp, int maxHp, int baseDmg, int inflame_str, int inflame_block, int weakness_amt, float x, float y) {
        super(NAME, ID, PossessedClass.Glorious, minHp, maxHp, baseDmg, -8F, 6F, 240.0F, 260.0F, x, y);
        this.firstInflame = false;
        this.isSummoned = true;
        this.firstMove = true;
        this.vanishTurn = -1;
        this.inflame_str = inflame_str;
        this.inflame_block = inflame_block;
        this.weakness_amt = weakness_amt;
        this.loadAnimation(SPEC_ATLAS, SPEC_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void initialDamages(int baseDamage) {
        this.searDmg = baseDmg;
        this.turnDmg = MathUtils.ceil(baseDamage / 3F);
        this.overflowDmg = 1;
        this.damage.add(1, new DamageInfo(this, this.searDmg));
        this.damage.add(2, new DamageInfo(this, this.turnDmg));
        this.damage.add(3, new DamageInfo(this, this.overflowDmg));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                this.addToBot(new ChangeStateAction(this, "InsaneAtk"));
                for (int i = 0; i < sear_count; i++) {
                    this.addToBot(new SpectreSearAction(new Burn(), this.damage.get(1), this, player, ManiacImageMaster.ColorDeviator(Color.GOLD, 0.2F, 0.4F)));
                }
                break;
            case 2:
                if (firstInflame) {
                    firstInflame = false;
                    int amounts = numRisksInDeck(ManiacRisksCard.Enum.THE_SHINIES);
                    if (amounts <= 0)
                        amounts += 2;
                    this.addToBot(new VFXAction(new InflameEffect(this)));
                    this.addToBotPower(this, this, new StrengthPower(this, amounts), amounts);
                    this.addToBotPower(this, this, new IntangiblePower(this, 2), 2);
                } else {
                    this.addToBotPower(this, this, new StrengthPower(this, inflame_str), inflame_str);
                    this.addToBot(new GainBlockAction(this, this, inflame_block));
                }
                break;
            case 3:
                this.addToBot(new ChangeStateAction(this, "InsaneAtk"));
                for (int i = 0; i < overflow_count; i++) {
                    playAttackSfx();
                    this.addToBotDamage(player, this.damage.get(3), AbstractGameAction.AttackEffect.FIRE);
                }
                this.addToBotPower(this, this, new PlatedArmorPower(this, overflow_count), overflow_count);
                break;
            case 4:
                if (turnCount <= 0) break;
                if (turnCount > 6)
                    this.addToBot(new ChangeStateAction(this, "InsaneAtk"));
                else
                    this.addToBot(new ChangeStateAction(this, "NormalAtk"));
                for (int i = 0; i < turnCount; i++) {
                    this.addToBotDamage(player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                    this.addToBotPower(player, this, new WeaknessPower(player, weakness_amt), weakness_amt);
                }
                break;
            case 5:
                this.addToBot(new VanishAction(this, false, Color.GOLD));
                break;
        }

        super.takeTurn();
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (isSummoned) {
            if (!hasPossibleMoveSets(this, num)) {
                if (firstMove) {
                    firstMove = false;
                    this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
                } else {
                    if (num < 23) {
                        if (!this.lastTwoMovesContain((byte)2))
                            this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                        else if (!this.lastMoveBefore(((byte)3)))
                            this.setMove(MOVES[2], (byte)3, Intent.ATTACK_BUFF, this.damage.get(3).base, overflow_count, true);
                        else if (!this.lastMoveBefore(((byte)4)) && AbstractDungeon.aiRng.randomBoolean() && turnCount > 1)
                            this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                        else 
                            this.getMove(AbstractDungeon.aiRng.random(78));
                    }
                    else if (num < 39) {
                        if (!this.lastMoveBefore((byte)1))
                            this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
                        else if (!this.lastTwoMovesContain((byte)4) && turnCount > 1)
                            this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                        else if (!this.lastMove((byte)3))
                            this.setMove(MOVES[2], (byte)3, Intent.ATTACK_BUFF, this.damage.get(3).base, overflow_count, true);
                        else
                            this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                    }
                    else if (num < 53) {
                        if (!this.lastMove((byte)1))
                            this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
                        else if (!this.lastMoveBefore((byte)2))
                            this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                        else if (AbstractDungeon.aiRng.randomBoolean() && turnCount > 1)
                            this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                        else
                            this.setMove(MOVES[2], (byte)3, Intent.ATTACK_BUFF, this.damage.get(3).base, overflow_count, true);
                    } else {
                        if (!this.lastTwoMovesContain((byte)4) && turnCount > 1)
                            this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                        else if (this.lastMove((byte)2) && !this.lastMoveBefore((byte)1))
                            this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
                        else
                            this.setMove(MOVES[2], (byte)3, Intent.ATTACK_BUFF, this.damage.get(3).base, overflow_count, true);
                    }
                }
            }
        } else {
            if (turnCount > vanishTurn) {
                this.setMove((byte)5, Intent.UNKNOWN);
            }
            else if (firstMove) {
                firstMove = false;
                this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
            } else {
                if (num < 21) {
                    if (!this.lastTwoMovesContain((byte)2))
                        this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                    else if (!this.lastMoveBefore(((byte)3)))
                        this.setMove(MOVES[2], (byte)3, Intent.ATTACK_BUFF, this.damage.get(3).base, overflow_count, true);
                    else if (!this.lastMoveBefore(((byte)4)) && AbstractDungeon.aiRng.randomBoolean() && turnCount > 1)
                        this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                    else
                        this.getMove(AbstractDungeon.aiRng.random(78));
                }
                else if (num < 34) {
                    if (!this.lastMoveBefore((byte)1))
                        this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
                    else if (!this.lastTwoMovesContain((byte)4) && turnCount > 1)
                        this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                    else if (!this.lastMove((byte)3))
                        this.setMove(MOVES[2], (byte)3, Intent.ATTACK_BUFF, this.damage.get(3).base, overflow_count, true);
                    else
                        this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                }
                else if (num < 47) {
                    if (!this.lastMove((byte)1))
                        this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
                    else if (!this.lastMoveBefore((byte)2))
                        this.setMove(MOVES[1], (byte)2, Intent.DEFEND_BUFF);
                    else if (AbstractDungeon.aiRng.randomBoolean() && turnCount > 1)
                        this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                    else
                        this.getMove(AbstractDungeon.aiRng.random(52));
                } else {
                    if (!this.lastTwoMovesContain((byte)4) && turnCount > 1)
                        this.setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(2).base, this.turnCount, true);
                    else if (this.lastMove((byte)2) && !this.lastMoveBefore((byte)1))
                        this.setMove(MOVES[0], (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base, sear_count, true);
                    else
                        this.setMove(MOVES[2], (byte)3, Intent.ATTACK_BUFF, this.damage.get(3).base, overflow_count, true);
                }
            }
        }
    }

    private void playAttackSfx() {
        int index = MathUtils.random(1, 3);
        switch (index) {
            case 1:
                CardCrawlGame.sound.play(TheManiac.makeID("SpectreIgniteEffect_v1"));
                break;
            case 2:
                CardCrawlGame.sound.play(TheManiac.makeID("SpectreIgniteEffect_v2"));
                break;
            case 3:
                CardCrawlGame.sound.play(TheManiac.makeID("SpectreIgniteEffect_v3"));
                break;
        }
    }

    @Override
    public void changeState(String key) {
        switch (key) {
            case "InsaneAtk":
                this.state.setAnimation(0, "InsaneAttack", false);
                this.state.setTimeScale(0.8F);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
            case "NormalAtk":
                this.state.setAnimation(0, "NormalAttack", false);
                this.state.setTimeScale(0.8F);
                this.state.addAnimation(0, "Idle", true, 0F);
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
    public void update() {
        super.update();
        if (!this.isDying &&!this.isVanishing && !this.firstInflame) {
            fire_timer -= Gdx.graphics.getDeltaTime();
            if (fire_timer < 0F) {
                this.fire_timer = 0.04F;
                AbstractDungeon.effectList.add(new SpectreInflameFireEffect(
                        this.skeleton.getX() + this.skeleton.findBone("coreFire").getWorldX(), 
                        this.skeleton.getY() + this.skeleton.findBone("coreFire").getWorldY(), 
                        ManiacImageMaster.ColorDeviator(Color.GOLD, 0.2F, 0.4F)));
            }
        }
    }
}
