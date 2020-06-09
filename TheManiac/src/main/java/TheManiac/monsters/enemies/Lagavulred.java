package TheManiac.monsters.enemies;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Lagavulred extends CustomMonster {
    public static final String ID = "maniac:Lagavulred";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String LAGAR_ATLAS = "maniacMod/images/monsters/enemies/Lagavulred/Lagavulred.atlas";
    private static final String LAGAR_JSON = "maniacMod/images/monsters/enemies/Lagavulred/skeleton.json";
    private static final int max_hp = 68;
    private static final int min_hp = 64;
    private static final int asc_maxHp = 74;
    private static final int asc_minHp = 70;
    private static final int siphon_frail = 1;
    private static final int siphon_block = 20;
    private static final int asc_frail = 2;
    private static final int asc_block = 25;
    private int claw_dmg;
    private int power_str;
    private int attack_counter;
    
    public Lagavulred(float x, float y) {
        super(NAME, ID, 88, 0.0F, -25.0F, 320.0F, 360.0F, null, x, y);
        this.type = EnemyType.ELITE;
        this.attack_counter = 0;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(asc_minHp, asc_maxHp);
        } else {
            this.setHp(min_hp, max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.power_str = 2;
        } else {
            this.power_str = 1;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.claw_dmg = 6;
        } else {
            this.claw_dmg = 5;
        }
        this.damage.add(new DamageInfo(this, this.claw_dmg));
        
        this.loadAnimation(LAGAR_ATLAS, LAGAR_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle_2", true);
        this.stateData.setMix("Attack", "Idle_2", 0.25F);
        this.stateData.setMix("Hit", "Idle_2", 0.25F);
        this.stateData.setMix("Idle_1", "Idle_2", 0.5F);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 1:
                this.attack_counter = 0;
                this.addToBot(new ChangeStateAction(this, "DEBUFF"));
                this.addToBot(new WaitAction(0.3F));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, asc_frail, true), asc_frail));
                    this.addToBot(new GainBlockAction(this, this, asc_block));
                } else {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, siphon_frail, true), siphon_frail));
                    this.addToBot(new GainBlockAction(this, this, siphon_block));
                }
                break;
            case 2:
                this.attack_counter++;
                this.addToBot(new ChangeStateAction(this, "ATTACK"));
                this.addToBot(new WaitAction(0.3F));
                for (i = 0; i < 2; i++) {
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                }
                break;
            case 3:
                this.attack_counter = 0;
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.power_str), this.power_str));
                        continue;
                    }
                    if (!m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.power_str), this.power_str));
                    }
                }
                break;
        }

        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if ((this.attack_counter == 0 || this.attack_counter == 2) && !this.lastMove((byte)1) && !this.lastMove((byte)3)) {
            if (num < 45) {
                if (!this.lastMove((byte)1)) {
                    this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
                } else {
                    this.setMove((byte)3, Intent.BUFF);
                }
            } else {
                if (!this.lastMove((byte)3)) {
                    this.setMove((byte)3, Intent.BUFF);
                } else {
                    this.setMove(MOVES[0], (byte)1, Intent.DEBUFF);
                }
            }
        } else {
            this.setMove(MOVES[1], (byte)2, Intent.ATTACK, this.damage.get(0).base, 2, true);
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
            this.updateHitbox(0.0F, -25.0F, 320.0F, 360.0F);
            this.state.setAnimation(0, "Coming_out", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.output > 0 && info.type == DamageInfo.DamageType.NORMAL && info.owner != null) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle_2", true, 0.0F);
        }
    }
}
