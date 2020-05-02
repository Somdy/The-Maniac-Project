package TheManiac.actions;

import TheManiac.cards.maniac_blue.weapon.*;
import TheManiac.character.TheManiacCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class DiscoverWeaponAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:DiscoverWeaponAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private static final Logger logger = LogManager.getLogger(DiscoverWeaponAction.class.getName());
    private AbstractPlayer player;
    private int counts;
    private int selects;
    private int upgrades;
    private int additionalUpgrades;
    private boolean random;

    public DiscoverWeaponAction(int numOfCards, int selects) {
        this.player = AbstractDungeon.player;
        this.counts = numOfCards;
        this.selects = selects;
        this.random = false;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }
    
    public DiscoverWeaponAction(int selects, boolean random) {
        this.player = AbstractDungeon.player;
        this.selects = selects;
        this.counts = 9;
        this.random = random;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player instanceof TheManiacCharacter) {
            upgrades += ((TheManiacCharacter) this.player).weaponUpgrades;
            if (upgrades > 3) {
                additionalUpgrades += ((TheManiacCharacter) this.player).weaponUpgrades;
            }
            logger.info("Weapon upgrades from player: " + ((TheManiacCharacter) this.player).weaponUpgrades);
        }
        
        logger.info("Current weapons upgrades: " + upgrades + " and " + additionalUpgrades);

        CardGroup weaponLibrary = initializeWeaponLibrary(upgrades, additionalUpgrades);

        if (this.duration == this.startDuration) {
            if (this.counts <= 0) {
                logger.info("No optional weapons. Report this if you see it.");
                logger.info("没有可选武器。如果你看到了这条提示，请反馈给作者。");
                this.isDone = true;
                return;
            }
            if (this.selects <= 0) {
                logger.info("Not allowed to obtain weapons. Report this if you see it.");
                logger.info("不可选取武器。如果你看到了这条提示，请反馈给作者。");
                this.isDone = true;
                return;
            }

            CardGroup randomWeapons = getRandomOptionalWeapons(weaponLibrary);
            if (random) {
                if (this.selects > 0) {
                    for (int i = 0; i < this.selects; i++) {
                        AbstractCard card = randomWeapons.getRandomCard(true);
                        if (this.player.hand.size() < 10) {
                            this.addToBot(new MakeTempCardInHandAction(card.makeStatEquivalentCopy(), 1));
                        }
                        else if (this.player.hand.size() >= 10) {
                            this.addToBot(new MakeTempCardInDiscardAction(card.makeStatEquivalentCopy(), 1));
                        }
                    }
                }
                this.isDone = true;
                return;
            }
            
            if (randomWeapons.size() <= this.selects) {
                logger.info("Your hands are full, adjusting weapons...");
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>(randomWeapons.group);
                for (AbstractCard c : cardsToMove) {
                    if (this.player.hand.size() == 10) {
                        randomWeapons.moveToDiscardPile(c);
                        this.player.createHandIsFullDialog();
                        continue;
                    }
                    randomWeapons.moveToHand(c, randomWeapons);
                }
                this.isDone = true;
                return;
            }
            randomWeapons.sortAlphabetically(true);
            randomWeapons.sortByRarityPlusStatusCardType(false);
            if (this.selects == 1) {
                AbstractDungeon.gridSelectScreen.open(randomWeapons, this.selects, true, TEXT[0]);
            }
            else {
                AbstractDungeon.gridSelectScreen.open(randomWeapons, this.selects, true, TEXT[1] + this.selects + TEXT[2]);
            }
            
            logger.info("Selected weapons");
            
            this.tickDuration();
            return;
        }
        
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (this.player.hand.size() == 10) {
                    weaponLibrary.moveToDiscardPile(card);
                    this.player.createHandIsFullDialog();
                    continue;
                }
                weaponLibrary.moveToHand(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        
        this.tickDuration();
    }

    private CardGroup initializeWeaponLibrary(int upgradeAmount, int additionalAmount) {
        CardGroup weaponGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        logger.info("Initialize weapon library...");

        AncientSphere ancientSphere = new AncientSphere();
        if (additionalAmount > 0) {
            ancientSphere.setPowersApply(upgradeAmount, additionalAmount);
        }
        else {
            ancientSphere.setPowersApply(upgradeAmount, 0);
        }
        weaponGroup.addToTop(ancientSphere);

        BigSword bigSword = new BigSword();
        bigSword.setSwordValues(upgradeAmount, additionalAmount);
        weaponGroup.addToTop(bigSword);

        BitingDagger bitingDagger = new BitingDagger();
        bitingDagger.setDaggerValues(upgradeAmount, additionalAmount > 0);
        weaponGroup.addToTop(bitingDagger);

        BurglaryToolkit burglaryToolkit = new BurglaryToolkit();
        burglaryToolkit.setBurglaryValues(upgradeAmount);
        weaponGroup.addToTop(burglaryToolkit);

        CursedStaff cursedStaff = new CursedStaff();
        cursedStaff.setStaffValues(upgradeAmount, additionalAmount);
        weaponGroup.addToTop(cursedStaff);
        
        DoomScythe doomScythe = new DoomScythe();
        if (additionalAmount > 0) {
            doomScythe.setScytheValues(upgradeAmount, additionalAmount, true);
        }
        else {
            doomScythe.setScytheValues(upgradeAmount, 0, false);
        }
        weaponGroup.addToTop(doomScythe);
        
        DualMachetes dualMachetes = new DualMachetes();
        dualMachetes.setMachetesValues(upgradeAmount, additionalAmount > 0);
        weaponGroup.addToTop(dualMachetes);
        
        HeavySkull heavySkull = new HeavySkull();
        heavySkull.setSkullValues(upgradeAmount, additionalAmount, additionalAmount > 2);
        weaponGroup.addToTop(heavySkull);
        
        ScorchingSpear scorchingSpear = new ScorchingSpear();
        scorchingSpear.setSpearValues(upgradeAmount, additionalAmount, additionalAmount > 0);
        weaponGroup.addToTop(scorchingSpear);
        
        logger.info("Done initializing weapon library!");

        return weaponGroup;
    }

    private CardGroup getRandomOptionalWeapons(CardGroup cardGroup) {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        logger.info("Now getting random weapons");

        while (tmpGroup.size() != this.counts) {
            AbstractCard tmpCard = cardGroup.getRandomCard(AbstractDungeon.cardRandomRng);
            if (!tmpGroup.contains(tmpCard)) {
                tmpGroup.addToTop(tmpCard);
            }
        }
        
        logger.info("Random weapons set!");
        return tmpGroup;
    }
}
