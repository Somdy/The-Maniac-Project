package TheManiac.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;

public class LacerateAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractPlayer p;
    private AttackEffect effect;
    
    public LacerateAction(DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.effect = effect;
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.DAMAGE;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (p.exhaustPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            AbstractCard cardToRemove = p.exhaustPile.getRandomCard(true);
            if (cardToRemove != null) {
                AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
                if (m != null && !m.isDeadOrEscaped()) {
                    this.addToBot(new VFXAction(new ClawEffect(m.hb.cX, m.hb.cY, Color.CYAN, Color.WHITE), 0.1F));
                    this.addToBot(new DamageAction(m, info));
                    p.exhaustPile.removeCard(cardToRemove);
                }
            }
        }
        this.tickDuration();
        if (this.isDone) {
            this.addToBot(new LacerateAction(info, effect));
        }
    }
}