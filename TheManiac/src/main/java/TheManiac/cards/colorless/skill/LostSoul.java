package TheManiac.cards.colorless.skill;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.relics.PossessedManuscripts;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

public class LostSoul extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("LostSoul");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/skill/lost_soul.png";
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 0;
    private int opt = 0;
    private List<TooltipInfo> tips = new ArrayList<>();
    
    public LostSoul() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.purgeOnUse = true;
        
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
    }

    public LostSoul(int index) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.purgeOnUse = true;

        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
        setDefaults(index);
    }
    
    public void setDefaults(int index) {
        switch (index) {
            case 1:
                this.name = EXTENDED_DESCRIPTION[2];
                this.rawDescription += EXTENDED_DESCRIPTION[5];
                this.modifyCostForCombat(1);
                this.cardsToPreview = new Dazed();
                this.opt = 1;
                break;
            case 2:
                this.name = EXTENDED_DESCRIPTION[3];
                this.rawDescription += EXTENDED_DESCRIPTION[6];
                this.modifyCostForCombat(2);
                this.cardsToPreview = new Wound();
                this.opt = 2;
                break;
            case 3:
                this.name = EXTENDED_DESCRIPTION[4];
                this.rawDescription += EXTENDED_DESCRIPTION[7];
                this.modifyCostForCombat(3);
                this.opt = 3;
                break;
        }
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (this.opt) {
            case 1:
                this.addToBot(new DiscardAction(p, p, 1, true, false));
                this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
                break;
            case 2:
                this.addToBot(new ExhaustAction(2, true, false, false));
                this.addToBot(new MakeTempCardInDrawPileAction(new Wound(), 2, true, true));
                break;
        }
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (this.opt == 3 && card != this) {
            return false;
        }
        
        return super.canPlay(card);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.opt == 1 && p.hand.size() < 2) {
            return false;
        }
        if (this.opt == 2 && p.hand.size() < 3) {
            return false;
        }
        return this.opt > 0;
    }

    @Override
    public void triggerOnExhaust() {
        if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof PossessedManuscripts) {
                    if (this.opt <= 0) {
                        this.opt = 1;
                    }
                    ((PossessedManuscripts) relic).modifyActiveAmount(3, this.opt);
                }
            }
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public boolean canEnchant() {
        return false;
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public AbstractCard makeCopy() {
        if (this.opt == 0) {
            return new LostSoul();
        }
        return new LostSoul(this.opt);
    }
}
