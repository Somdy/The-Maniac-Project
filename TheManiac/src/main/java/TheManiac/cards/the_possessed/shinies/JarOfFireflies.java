package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.relics.PossessedManuscripts;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.scene.FireFlyEffect;

public class JarOfFireflies extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("JarOfFireflies");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/jar_of_fireflies.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 3;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public JarOfFireflies() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.getRelic(PossessedManuscripts.ID) != null) {
            for (AbstractRelic relic : p.relics) {
                if (relic instanceof PossessedManuscripts) {
                    relic.flash();
                    ((PossessedManuscripts) relic).modifyActiveAmount(0, 1D);
                    ((PossessedManuscripts) relic).activeEffects.set(0, true);
                    ((PossessedManuscripts) relic).updateEffectDescription();
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            this.addToBot(new VFXAction(new FireFlyEffect(Color.GOLD)));
        }
        
        if (AbstractDungeon.player.masterDeck.findCardById(JarOfFireflies.ID) != null) {
            AbstractCard card = AbstractDungeon.player.masterDeck.findCardById(JarOfFireflies.ID);
            AbstractDungeon.player.masterDeck.removeCard(card);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new JarOfFireflies();
    }
}
