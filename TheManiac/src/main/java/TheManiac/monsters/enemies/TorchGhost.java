package TheManiac.monsters.enemies;

import TheManiac.actions.SeparateSoulAction;
import TheManiac.vfx.TorchGhostEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.TorchHeadFireEffect;

public class TorchGhost extends CustomMonster {
    public static final String ID = "maniac:torchGhost";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String TORCH_ATLAS = "maniacMod/images/monsters/enemies/torchGhost/torchGhost.atlas";
    private static final String TORCH_JSON = "maniacMod/images/monsters/enemies/torchGhost/skeleton.json";
    private static final int max_hp = 32;
    private static final int min_hp = 28;
    private static final int asc_maxHp = 38;
    private static final int asc_minHp = 34;
    private int tackle_dmg;
    private int separate_soul;
    private boolean firstMove;
    private float fire_time = 0F;

    public TorchGhost(float x, float y) {
        super(NAME, ID, 15, 0F, -5F, 180F, 140F, null, x, y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(asc_minHp, asc_maxHp);
        } else {
            this.setHp(min_hp, max_hp);
        }

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.separate_soul = 3;
            this.firstMove = true;
        } else {
            this.separate_soul = 2;
            this.firstMove = false;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.tackle_dmg = 5;
        } else {
            this.tackle_dmg = 4;
        }
        this.damage.add(new DamageInfo(this, this.tackle_dmg));
        
        this.loadAnimation(TORCH_ATLAS, TORCH_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                this.addToBot(new SeparateSoulAction(this.separate_soul));
                break;
            case 2:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
        }
        
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            this.setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
        } else {
            if (num < 15) {
                if (!this.lastMove((byte)1)) {
                    this.setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
                } else {
                    this.setMove((byte)2, Intent.ATTACK, this.damage.get(0).base);
                }
            } else {
                this.setMove((byte)2, Intent.ATTACK, this.damage.get(0).base);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.isDying) {
            this.fire_time -= Gdx.graphics.getDeltaTime();
            if (this.fire_time < 0.0F) {
                this.fire_time = 0.04F;
                AbstractDungeon.effectList.add(new TorchGhostEffect(this.skeleton.getX() + this.skeleton.findBone("fireslot").getX() + 10.0F * Settings.scale, this.skeleton.getY() + this.skeleton.findBone("fireslot").getY() + 110.0F * Settings.scale));
            }
        }
    }
}
