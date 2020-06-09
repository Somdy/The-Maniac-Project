package TheManiac.helper;

import TheManiac.cards.colorless.skill.LostSoul;
import TheManiac.cards.status.Unidentified;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import TheManiac.cards.the_possessed.possessed.BookClub;
import TheManiac.cards.the_possessed.possessed.Gifts;
import TheManiac.cards.the_possessed.possessed.Leviathans;
import TheManiac.cards.the_possessed.possessed.PossessedCurse;
import TheManiac.cards.the_possessed.risks.*;
import TheManiac.cards.the_possessed.shinies.*;
import TheManiac.cards.the_possessed.uncertainties.*;
import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ThePossessedInitializer {
    private static final Logger logger = LogManager.getLogger(ThePossessedInitializer.class.getName());
    public static ArrayList<ManiacRisksCard> Shinies = new ArrayList<>();
    public static ArrayList<ManiacRisksCard> Uncertainties = new ArrayList<>();
    public static ArrayList<ManiacRisksCard> Risks = new ArrayList<>();
    public static ArrayList<ManiacRisksCard> Possessed = new ArrayList<>();

    public static void addCards() {
        Shinies.add(new CelebrationBus());
        Shinies.add(new RocketshipOfLearning());
        Shinies.add(new ToyExterminator());
        Shinies.add(new Scavenger());
        Shinies.add(new CreationScience());
        Shinies.add(new JarOfFireflies());
        Shinies.add(new EnchantedFruit());
        Shinies.add(new CorruptingMist());
        Shinies.add(new PrimordialGlyph());
        Shinies.add(new WitchHour());
        logger.info("===正在获得新荣誉");
        for (AbstractCard card : Shinies) {
            BaseMod.addCard(card.makeCopy());
            UnlockTracker.unlockCard(card.cardID);
        }
        logger.info("所有荣誉已获得===");
        
        Uncertainties.add(new MysterySeasoning());
        Uncertainties.add(new MysteryRecorder());
        Uncertainties.add(new TopologicalEnergy());
        Uncertainties.add(new TheTerribleSecret());
        Uncertainties.add(new Translocation());
        Uncertainties.add(new BrokenMagnet());
        Uncertainties.add(new RailroadXing());
        Uncertainties.add(new Stellarite());
        Uncertainties.add(new NatureNourish());
        Uncertainties.add(new ShadowVisions());
        logger.info("===正在预测新未知");
        for (AbstractCard card : Uncertainties) {
            BaseMod.addCard(card.makeCopy());
            UnlockTracker.unlockCard(card.cardID);
        }
        logger.info("所有未知已排除===");
        
        Risks.add(new BookOfDarkness());
        Risks.add(new PsychicNightmare());
        Risks.add(new DustAndAshes());
        Risks.add(new EvilEntity());
        Risks.add(new ToyLeperchaun());
        Risks.add(new FashionableSunglasses());
        Risks.add(new EtherDrake());
        Risks.add(new FuryOfAir());
        Risks.add(new GlacialMysteries());
        Risks.add(new UnwillingSacrifice());
        logger.info("===正在估算新风险");
        for (AbstractCard card : Risks) {
            BaseMod.addCard(card.makeCopy());
            UnlockTracker.unlockCard(card.cardID);
        }
        logger.info("所有风险已清除===");
        
        Possessed.add(new BookClub());
        Possessed.add(new Gifts());
        Possessed.add(new Leviathans());
        logger.info("===正在稳定群魔的情绪");
        for (AbstractCard card : Possessed) {
            BaseMod.addCard(card.makeCopy());
            UnlockTracker.unlockCard(card.cardID);
        }
        logger.info("所有群魔情绪已安抚===");
        
        logger.info("===正在添加特殊的……");
        BaseMod.addCard(new NatureCorrupt());
        BaseMod.addCard(new LostSoul());
        BaseMod.addCard(new PossessedCurse());
        BaseMod.addCard(new Unidentified());
        logger.info("已添加所有特殊的……===");
    }
}
