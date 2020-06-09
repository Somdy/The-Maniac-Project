package TheManiac.actions.PossessedMonsterAction;

import TheManiac.cards.the_possessed.possessed.PossessedCurse;
import TheManiac.character.TheManiacCharacter;
import TheManiac.monsters.possessed_enemies.AbstractPossessedMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class ShufflePossessedCurseAction extends AbstractGameAction {
    private AbstractPossessedMonster m;
    private int minOpt;
    private int maxOpt;
    private int baseDmg;
    private int baseMagics;
    private AbstractPlayer p = AbstractDungeon.player;
    
    public ShufflePossessedCurseAction(AbstractPossessedMonster m, int maxOpt, int baseDmg, int baseMagics) {
        this.m = m;
        this.minOpt = 1;
        this.maxOpt = maxOpt;
        this.baseDmg = baseDmg;
        this.baseMagics = baseMagics;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (p.masterDeck == null) {
                this.isDone = true;
                return;
            }
            
            switch (m.ownClass) {
                case Glorious:
                    baseDmg = (int) Math.ceil(baseDmg / 2D);
                    baseMagics = (int) Math.ceil(baseMagics / 2D);
                    break;
                case Uncertain:
                    minOpt++;
                    break;
                case Risky: 
                    maxOpt++;
                    baseDmg++;
                    baseMagics++;
                    break;
            }
            
            if (maxOpt > 7)
                maxOpt = 7;
            if (minOpt > maxOpt)
                minOpt -= Math.abs(minOpt - maxOpt);
            if (minOpt < 1)
                minOpt =1;
            
            int opt = AbstractDungeon.cardRandomRng.random(minOpt, maxOpt);
            PossessedCurse curse = new PossessedCurse(opt, baseDmg, baseMagics);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
        }
        
        this.isDone = true;
    }
}
