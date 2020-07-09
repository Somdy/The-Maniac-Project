package TheManiac.cards.maniac_blue.power;

import TheManiac.TheManiac;
import TheManiac.cards.colorless.skill.Unravel;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.PerceptionPower;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.UUID;

public class UniversalPerception extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("UniversalPerception");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/power/universal_perception.png";
    private static final String SHROUD_IMG = "maniacMod/images/1024portraits/curse/ravelled.png";
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 4;
    private AbstractCard c1;
    private AbstractCard c2;

    public UniversalPerception() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
        this.isShroud = true;
    }

    public UniversalPerception(boolean upgraded) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
        this.isShroud = true;

        if (upgraded) {
            this.upgrade();
        }
    }

    public UniversalPerception(UUID corUUID, boolean upgraded) {
        super(ID, EXTENDED_DESCRIPTION[1], SHROUD_IMG, 1, EXTENDED_DESCRIPTION[0], 
                CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.NONE);
        this.isShroud = true;
        this.shrouded = true;
        this.storedUUID = corUUID;

        if (corUUID != null) {
            this.cardsToPreview = new UniversalPerception(upgraded);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!shrouded) {
            this.addToBot(new ApplyPowerAction(p, p, new PerceptionPower(p, 1, this.upgraded)));
        }
    }

    @Override
    public void triggerWhenDrawn() {
        if (shrouded) {
            this.modifyCostForCombat(
                    AbstractDungeon.cardRandomRng.random(Math.min(0, this.cost - 1), this.cost + 1));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return !shrouded;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (!shrouded) return true;

        if (findShroudCard(this.storedUUID))
            return card.costForTurn != this.costForTurn && (!card.freeToPlayOnce || !this.freeToPlayOnce);
        else return true;
    }

    @Override
    public void upgrade() {
        if (!upgraded && !shrouded) {
            this.upgradeName();
            this.rawDescription = UPGRADED_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void hover() {
        c1 = new Unravel();
        c2 = new UniversalPerception(null, this.upgraded);
        if (this.upgraded) c1.upgrade();
        super.hover();
    }

    @Override
    public void unhover() {
        super.unhover();
        c1 = null;
        c2 = null;
    }

    @Override
    public void renderCardTip(SpriteBatch sb) {
        super.renderCardTip(sb);

        if (this.isLocked || (AbstractDungeon.player != null && (AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode))) {
            return;
        }

        float tmpScale = 0.5F;
        float y1 = this.current_y + this.hb.height * 0.25F;
        float y2 = this.current_y - this.hb.height * 0.25F;

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP) {
            y1 = this.current_y - this.hb.height * 0.25F;
            y2 = this.current_y + this.hb.height * 0.25F;
        }

        float x1 = -this.hb.width * 0.75F;
        
        if (this.current_x > Settings.WIDTH * 0.75F) x1 = -x1;
        
        float PosX1 = this.current_x + x1;
        
        if (c1 != null) {
            c1.drawScale = tmpScale;
            c1.current_x = PosX1;
            c1.current_y = y1;
            c1.render(sb);
        }

        if (c2 != null) {
            c2.drawScale = tmpScale;
            c2.current_x = PosX1;
            c2.current_y = y2;
            c2.render(sb);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        if (shrouded) return new UniversalPerception(null, false);
        return new UniversalPerception();
    }

    @Override
    public AbstractCard makeShroudCopy() {
        AbstractCard card = new UniversalPerception(this.uuid, this.upgraded);

        if (upgraded) card.upgrade();

        return card;
    }
}
