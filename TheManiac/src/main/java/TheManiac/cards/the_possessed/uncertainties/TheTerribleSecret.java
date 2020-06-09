package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.actions.ApplyRandomDebuffAction;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class TheTerribleSecret extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("TheTerribleSecret");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/the_terrible_secret.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 1;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public TheTerribleSecret() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int times = 0;
        
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                times++;
            }
        }
        
        if (anyEnemyHasArtifact()) {
            this.addToBot(new ApplyRandomDebuffAction(m, p, 2, true, times));
        } else {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                this.addToBot(new ApplyRandomDebuffAction(p, monster, 2, true, times));
            }
        }
    }
    
    private boolean anyEnemyHasArtifact() {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.hasPower(ArtifactPower.POWER_ID)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheTerribleSecret();
    }
}
