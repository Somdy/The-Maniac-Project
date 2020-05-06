package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.monsters.minions.GremlinDefender;
import TheManiac.monsters.minions.GremlinFat;
import TheManiac.monsters.minions.GremlinWarrior;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;

import java.util.ArrayList;

public class GremlinCompanion extends AbstractManiacCard {
    public static final String ID = "maniac:GremlinCompanion";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/gremlin_companion.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    
    public GremlinCompanion() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.isEnchanter = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        try {
            if (p instanceof AbstractPlayerWithMinions) {
                AbstractPlayerWithMinions player = (AbstractPlayerWithMinions)p;
                AbstractFriendlyMonster gremlin = getGremlin(getRandomGremlinName(), (((AbstractPlayerWithMinions)p).getMinions()).monsters.size());
                gremlin.usePreBattleAction();
                player.addMinion(gremlin);
            }
        } catch (Exception e) {
            System.out.println("Failed to summon a gremlin. Catching: ");
            e.printStackTrace();
        }
    }

    private String getRandomGremlinName() {
        ArrayList<String> gremlinsPool = new ArrayList<>();
        
        gremlinsPool.add(GremlinDefender.ID);
        gremlinsPool.add(GremlinFat.ID);
        gremlinsPool.add(GremlinWarrior.ID);

        Random minionSummonRng = new Random(Settings.seed + (long)AbstractDungeon.floorNum + 
                (long)gremlinsPool.size() + (long)AbstractDungeon.actionManager.cardsPlayedThisCombat.size());
        
        System.out.println("Summon RNG: " + minionSummonRng);

        return gremlinsPool.get(minionSummonRng.random(0, gremlinsPool.size() - 1));
    }

    private AbstractFriendlyMonster getGremlin(String gremlinName, int num) {
        AbstractFriendlyMonster minion;
        switch (gremlinName) {
            case GremlinDefender.ID:
                GremlinDefender gremlinDefender = new GremlinDefender(num * 80F);
                if (this.upgraded) {
                    gremlinDefender.setGremlinValues(AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2));
                }
                minion = gremlinDefender;
                break;
            case GremlinFat.ID:
                GremlinFat gremlinFat = new GremlinFat(num * 80F);
                if (this.upgraded) {
                    gremlinFat.setGremlinValues(AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2));
                }
                minion = gremlinFat;
                break;
            case GremlinWarrior.ID:
                GremlinWarrior gremlinWarrior = new GremlinWarrior(num * 80F);
                if (this.upgraded) {
                    gremlinWarrior.setGremlinValues(AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2));
                }
                minion = gremlinWarrior;
                break;
            default:
                GremlinDefender gremlinDefender1 = new GremlinDefender(num * 80F);
                if (this.upgraded) {
                    gremlinDefender1.setGremlinValues(AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2), AbstractDungeon.miscRng.random(2));
                }
                minion = gremlinDefender1;
        }

        return minion;
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GremlinCompanion();
    }
}
