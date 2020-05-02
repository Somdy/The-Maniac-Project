package TheManiac.actions;

import TheManiac.cards.maniac_blue.attack.SpectralFlash;
import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpectralFlashAction extends AbstractGameAction {
    private int Dmg;
    private int baseDmg;
    private int DmgIncrease;
    private AbstractMonster targetMonster;
    
    public SpectralFlashAction(int Dmg, int baseDmg, int DmgIncrease, AbstractCreature source, AbstractMonster targetMonster) {
        this.Dmg = Dmg;
        this.baseDmg = baseDmg;
        this.DmgIncrease = DmgIncrease;
        this.targetMonster = targetMonster;
        this.source = source;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.targetMonster != null) {
            SpectralFlash spectralFlash = new SpectralFlash();
            spectralFlash.setAdditionalDmg(this.DmgIncrease + this.baseDmg);
            AbstractCard tmp = spectralFlash.makeStatEquivalentCopy();
            
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(tmp, 1, true, true));
            
            AbstractDungeon.actionManager.addToBottom(new DamageAction(this.targetMonster, new DamageInfo(this.source, this.Dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            
            if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.source, 1, false));
            }
        }
        
        this.isDone = true;
    }
}
