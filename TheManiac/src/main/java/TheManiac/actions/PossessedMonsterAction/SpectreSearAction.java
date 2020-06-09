package TheManiac.actions.PossessedMonsterAction;

import TheManiac.TheManiac;
import TheManiac.helper.ManiacImageMaster;
import TheManiac.vfx.PossessedVfx.SpectreFireBallEffect;
import TheManiac.vfx.PossessedVfx.SpectreIgniteFireEffect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class SpectreSearAction extends AbstractGameAction {
    private AbstractCard cardToShuffle;
    private DamageInfo info;
    private AbstractCreature target;
    private Color color;
    
    public SpectreSearAction(AbstractCard cardToShuffle, DamageInfo info, AbstractCreature source, AbstractCreature target, Color color) {
        this.cardToShuffle = cardToShuffle;
        this.info = info;
        this.source = source;
        this.target = target;
        this.color = color;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.effectList.add(new SpectreFireBallEffect(source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY, color));
            AbstractDungeon.effectList.add(new SpectreIgniteFireEffect(
                    target.hb.cX + MathUtils.random(-120.0F, 120.0F) * Settings.scale,
                    target.hb.cY + MathUtils.random(-120.0F, 120.0F) * Settings.scale, color));
            playAttackSfx();
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_LIGHT, false));
        }
        
        this.tickDuration();
        if (this.isDone) {
            target.damage(info);
            if (target.lastDamageTaken > 0 && target == AbstractDungeon.player)
                this.addToBot(new MakeTempCardInDrawPileAction(cardToShuffle, 1, true, true));
            this.addToTop(new WaitAction(0.1F));
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
}
