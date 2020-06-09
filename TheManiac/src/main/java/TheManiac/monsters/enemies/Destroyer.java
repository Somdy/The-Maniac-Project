package TheManiac.monsters.enemies;

import TheManiac.vfx.DestroyerFireEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Destroyer extends CustomMonster {
    public static final String ID = "maniac:Destroyer";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String DEST_ATLAS = "maniacMod/images/monsters/enemies/destroyer/destroyer.atlas";
    private static final String DEST_JSON = "maniacMod/images/monsters/enemies/destroyer/skeleton.json";
    private static final int max_hp = 30;
    private static final int asc_maxHp = 35;
    private int shock_dmg;
    private int shackle_str;
    private float fireTimer = 0F;
    private int turnCount = 0;
    private boolean firstMove;
    
    public Destroyer(float x, float y) {
        super(NAME, ID, 30, -8.0F, -10.0F, 150.0F, 150.0F, null, x, y);
        this.firstMove = true;
        
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(asc_maxHp);
        } else {
            this.setHp(max_hp);
        }
        
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.shackle_str = 3;
        } else {
            this.shackle_str = 2;
        }
        
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.shock_dmg = 8;
        } else {
            this.shock_dmg = 7;
        }
        this.damage.add(new DamageInfo(this, this.shock_dmg));
        
        this.loadAnimation(DEST_ATLAS, DEST_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new ExplosivePower(this, 3)));
    }

    @Override
    public void takeTurn() {
        this.turnCount++;
        switch (this.nextMove) {
            case 1:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case 2:
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.shackle_str), -this.shackle_str));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new GainStrengthPower(AbstractDungeon.player, this.shackle_str), this.shackle_str));
                break;
            case 3:
                this.addToBot(new MakeTempCardInDiscardAction(new VoidCard(), 1));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    this.addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
                }
                break;
            default:    
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (this.turnCount < 2) {
            if (this.firstMove) {
                this.firstMove = false;
                this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
            } else {
                if (num < 45F) {
                    this.setMove((byte)2, Intent.STRONG_DEBUFF);
                } else {
                    this.setMove((byte)3, Intent.DEBUFF);
                }
            }
        } else {
            this.setMove((byte)4, Intent.UNKNOWN);
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.isDying) {
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if (this.fireTimer < 0F) {
                this.fireTimer = 0.07F;
                AbstractDungeon.effectList.add(new DestroyerFireEffect(this.skeleton.getX() + 15F, this.skeleton.getY() + 60F));
            }
        }
    }
}
