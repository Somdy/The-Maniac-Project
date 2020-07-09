package TheManiac.cards.maniac_blue.attack;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;

import java.util.ArrayList;
import java.util.UUID;

public class TorturousScreams extends AbstractManiacCard {
    public static final String ID = "maniac:TorturousScreams";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/torturous_screams.png";
    private static final String SHROUD_IMG = "maniacMod/images/1024portraits/maniac_blue/attack/shrouded_scythe.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int DAMAGE = 4;

    public TorturousScreams() {
        super(ID, NAME, IMG_PATH, 1, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.isShroud = true;
        this.shrouded = false;
        this.exhaust = true;
        
        this.cardsToPreview = new TorturousScreams(null, this.upgraded);
    }

    public TorturousScreams(boolean upgraded) {
        super(ID, NAME, IMG_PATH, 1, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.isShroud = true;
        this.shrouded = false;
        this.exhaust = true;

        if (upgraded) {
            this.upgrade();
        }
    }

    public TorturousScreams(UUID corUUID, boolean upgraded) {
        super(ID, EXTENDED_DESCRIPTION[1], SHROUD_IMG, 2, EXTENDED_DESCRIPTION[0], TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 6;
        this.isShroud = true;
        this.shrouded = true;
        this.storedUUID = corUUID;
        this.exhaust = true;

        if (corUUID != null) {
            this.cardsToPreview = new TorturousScreams(upgraded);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> removeList = new ArrayList<>();
        int count = 0;
        
        if (!shrouded) {
            for (AbstractCard card : p.exhaustPile.group) {
                if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).isShroud && !((AbstractManiacCard) card).shrouded) {
                    count++;
                    removeList.add(card);
                }
            }
            
            this.addToBot(new SFXAction("INTIMIDATE"));
            this.addToBot(new VFXAction(p, new IntimidateEffect(p.hb.cX, p.hb.cY), 1.0F));
            this.addToBot(new DamageAllEnemiesAction(p, 
                    DamageInfo.createDamageMatrix(this.damage * count, true, false), 
                    DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
            
            p.exhaustPile.group.removeAll(removeList);
            removeList.clear();
        }
        
        if (shrouded && findShroudCard(this.storedUUID)) {
            for (AbstractCard card : p.drawPile.group) {
                if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).shrouded) {
                    count++;
                    this.addToBot(new ExhaustSpecificCardAction(card, p.drawPile, true));
                }
            }
            
            for (AbstractCard card : p.hand.group) {
                if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).shrouded) {
                    count++;
                    this.addToBot(new ExhaustSpecificCardAction(card, p.hand, true));
                }
            }

            for (AbstractCard card : p.discardPile.group) {
                if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).shrouded) {
                    count++;
                    this.addToBot(new ExhaustSpecificCardAction(card, p.discardPile, true));
                }
            }
            
            for (int i = 0; i < count; i++) {
                this.addToBot(new DamageRandomEnemyAction(new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!shrouded) return true;

        return findShroudCard(this.storedUUID);
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        if (shrouded) return new TorturousScreams(null, false);
        return new TorturousScreams();
    }

    @Override
    public AbstractCard makeShroudCopy() {
        AbstractCard card = new TorturousScreams(this.uuid, this.upgraded);
        
        if (upgraded) card.upgrade();
        
        return card;
    }
}
