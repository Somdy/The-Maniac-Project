package TheManiac.cards.temp;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FortressExpedition extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("FortressExpedition");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/enigmatic_fortress.png";
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 0;
    private int opt;
    
    public FortressExpedition() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.purgeOnUse = true;
        opt = 0;
        initData();
    }

    public FortressExpedition(int opt) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.purgeOnUse = true;
        this.opt = opt;
        initData();
    }
    
    public void initData() {
        switch (opt) {
            case 1:
                this.rawDescription = EXTENDED_DESCRIPTION[0];
                break;
            case 2:
                this.rawDescription = EXTENDED_DESCRIPTION[1];
                break;
            case 3:
                this.rawDescription = EXTENDED_DESCRIPTION[2];
                break;
            default:
                this.rawDescription = EXTENDED_DESCRIPTION[3];
                break;
        }
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        switch (opt) {
            case 1:
                this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10));
                this.addToBot(new MakeTempCardInHandAction(new FortressExpedition2(1), 1));
                this.addToBot(new MakeTempCardInHandAction(new FortressExpedition2(2), 1));
                break;
            case 2:
                for (int i = 0; i < 2; i++) {
                    if (!AbstractDungeon.player.hand.isEmpty()) {
                        AbstractDungeon.player.hand.moveToExhaustPile(AbstractDungeon.player.hand.getRandomCard(true));
                    }
                    else if (!AbstractDungeon.player.discardPile.isEmpty()) {
                        AbstractDungeon.player.discardPile.moveToExhaustPile(AbstractDungeon.player.discardPile.getRandomCard(true));
                    }
                    else if (!AbstractDungeon.player.drawPile.isEmpty()) {
                        AbstractDungeon.player.drawPile.moveToExhaustPile(AbstractDungeon.player.drawPile.getRandomCard(true));
                    }
                }
                break;
            case 3:
                for (int i = 0; i < 3; i++) {
                    if (!AbstractDungeon.player.drawPile.isEmpty()) {
                        AbstractDungeon.player.drawPile.moveToDiscardPile(AbstractDungeon.player.drawPile.getRandomCard(true));
                    }
                    else if (!AbstractDungeon.player.hand.isEmpty()) {
                        AbstractDungeon.player.hand.moveToDiscardPile(AbstractDungeon.player.hand.getRandomCard(true));
                    }
                }
                break;
            default:
                break;
        }
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
        return new FortressExpedition();
    }
}
