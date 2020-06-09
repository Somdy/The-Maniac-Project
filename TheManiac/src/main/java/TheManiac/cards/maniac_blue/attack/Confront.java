package TheManiac.cards.maniac_blue.attack;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class Confront extends AbstractManiacCard {
    public static final String ID = "maniac:Confront";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/confront.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 0;
    private static final int Dmg = 4;
    private static final int powers= 4;
    private static final int upgrade_amt = 2;
    private List<TooltipInfo> tips;
    
    public Confront() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = Dmg;
        this.magicNumber = this.baseMagicNumber = powers;
        this.cardsToPreview = new Strike_Maniac();
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[3]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                Strike_Maniac strike = new Strike_Maniac();
                strike.setWeaknessValues(Confront.this.magicNumber);
                strike.exhaust = true;
                strike.isEthereal = true;
                strike.purgeOnUse = true;
                this.addToBot(new MakeTempCardInHandAction(strike.makeStatEquivalentCopy(), 1));
                this.isDone = true;
            }
        });
        
        if (enchanted) {
            if (this.enchantment == 1) {
                if (m.currentBlock > 0) {
                    this.addToBot(new DamageAction(m, new DamageInfo(p, this.enchantNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
            } else {
                this.addToBot(new ApplyPowerAction(m, p, new FrailPower(m, this.enchantNumber, false), this.enchantNumber));
            }
        }
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 2)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(4);
        } else {
            this.modifyEnchants(2);
        }
        initializeDescription();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeDamage(upgrade_amt);
            this.upgradeMagicNumber(upgrade_amt);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Confront();
    }
}
