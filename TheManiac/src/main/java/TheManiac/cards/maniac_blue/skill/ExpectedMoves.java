package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.ApplyRandomDebuffAction;
import TheManiac.actions.DetectAction;
import TheManiac.actions.FlashbackAction;
import TheManiac.actions.TrackAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class ExpectedMoves extends AbstractManiacCard {
    public static final String ID = "maniac:ExpectedMoves";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/expected_moves.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int track = 3;
    private static final int upgrade_track = 1;
    private List<TooltipInfo> tips;
    
    public ExpectedMoves() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = track;
        this.isUnreal = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (enchanted && this.enchantment == 1) {
            this.magicNumber += this.enchantNumber;
        }
        this.addToBot(new DetectAction(this.magicNumber, AbstractDungeon.player.drawPile, CardType.SKILL));
        if (isInLimbo()) {
            this.addToBot(new FlashbackAction(this, this.magicNumber, false, CardType.SKILL));
        }
        
        if (enchanted) {
            if (this.enchantment == 3) {
                this.addToBot(new ChangeStanceAction(new LimboStance()));
                this.addToBot(new GainBlockAction(p, this.enchantNumber));
            }
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (enchanted && this.enchantment == 2) {
            if (c.type == CardType.SKILL) {
                AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(true);
                if (monster != null) {
                    this.addToBot(new ApplyRandomDebuffAction(AbstractDungeon.player, monster, 1, false, this.enchantNumber));
                }
            }
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isInLimbo()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
        else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 3)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                case 2:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(1);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(1);
        } else {
            this.modifyEnchants(4);
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
            this.upgradeMagicNumber(upgrade_track);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ExpectedMoves();
    }
}
