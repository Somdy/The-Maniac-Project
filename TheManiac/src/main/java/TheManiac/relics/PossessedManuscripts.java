package TheManiac.relics;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.possessed.BookClub;
import TheManiac.cards.the_possessed.possessed.Gifts;
import TheManiac.cards.the_possessed.possessed.Leviathans;
import TheManiac.cards.the_possessed.risks.AbstractRisksCard;
import TheManiac.cards.the_possessed.risks.BookOfDarkness;
import TheManiac.cards.the_possessed.risks.EtherDrake;
import TheManiac.cards.the_possessed.risks.FashionableSunglasses;
import TheManiac.cards.the_possessed.shinies.*;
import TheManiac.cards.the_possessed.uncertainties.AbstractUncertaintiesCard;
import TheManiac.cards.the_possessed.uncertainties.BrokenMagnet;
import TheManiac.cards.the_possessed.uncertainties.Stellarite;
import TheManiac.cards.the_possessed.uncertainties.TheTerribleSecret;
import TheManiac.monsters.possessed_enemies.AbstractPossessedMonster;
import TheManiac.monsters.possessed_enemies.RiskySpectre;
import TheManiac.rewards.MixesReward;
import TheManiac.rewards.PossessionsReward;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.LightBulbEffect;
import com.megacrit.cardcrawl.vfx.scene.FireFlyEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class PossessedManuscripts extends CustomRelic implements CustomSavable<ArrayList<ArrayList>> {
    private static final Logger logger = LogManager.getLogger(PossessedManuscripts.class.getName());
    public static final String ID = TheManiac.makeID("PossessedManuscripts");
    private static final String IMG_PATH = "maniacMod/images/relics/possessedManuscripts.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/possessedManuscripts_outline.png";
    private final AbstractPlayer player = AbstractDungeon.player;
    private String INFO = null;
    public ArrayList<ArrayList> collections = new ArrayList<>();
    public ArrayList<Double> activeAmounts = new ArrayList<>();
    public ArrayList<Boolean> activeEffects = new ArrayList<>();
    
    public boolean fireflyEffect;
    public boolean sunglassesEffect;
    public boolean witchEffect;
    public boolean lostSoulEffect;
    public boolean nourishEffect;
    public double fireflyHeal;
    public double sunglassesChance;
    public double witchNum;
    public double lostSouls;
    
    public PossessedManuscripts() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.SPECIAL, LandingSound.FLAT);
        this.fireflyEffect = false;
        this.sunglassesEffect = false;
        this.witchEffect = false;
        this.lostSoulEffect = false;
        this.nourishEffect = false;
        this.fireflyHeal = 0D;
        this.sunglassesChance = 0D;
        this.witchNum = 0D;
        this.lostSouls = 0D;
        preLoadActiveEffects();
        preLoadActiveAmounts();
        preLoadCollections();
        updateEffectDescription();
    }
    
    private void preLoadActiveEffects() {
        this.activeEffects.add(0, fireflyEffect);
        this.activeEffects.add(1, sunglassesEffect);
        this.activeEffects.add(2, witchEffect);
        this.activeEffects.add(3, lostSoulEffect);
        this.activeEffects.add(4, nourishEffect);
        
        for (int i = 0; i < this.activeEffects.size(); i++) {
            this.activeEffects.set(i, false);
        }
    }
    
    private void preLoadActiveAmounts() {
        this.activeAmounts.add(0, fireflyHeal);
        this.activeAmounts.add(1, sunglassesChance);
        this.activeAmounts.add(2, witchNum);
        this.activeAmounts.add(3, lostSouls);
        
        for (int i = 0; i < this.activeAmounts.size(); i++) {
            this.activeAmounts.set(i, 0D);
        }
    }
    
    private void preLoadCollections() {
        this.collections.add(0, this.activeEffects);
        this.collections.add(1, this.activeAmounts);
    }
    
    public void modifyActiveAmount(int index, double amount) {
        this.activeAmounts.set(index, this.activeAmounts.get(index) + amount);
        updateEffectDescription();
    }
    
    public void changeActiveAmount(int index, double amount) {
        this.activeAmounts.set(index, amount);
        updateEffectDescription();
    }
    
    public int getAdditionalHp(AbstractMonster m, int minHp, int maxHp) {
        float floors = AbstractDungeon.floorNum;
        switch (AbstractDungeon.actNum) {
            case 1:
                floors *= 1.25F;
                break;
            case 2:
                floors *= 1.5F;
                break;
            case 3:
                floors *= 1.75F;
                break;
            default:
                floors *= 2F;
        }
        
        if (m instanceof AbstractPossessedMonster) {
            switch (((AbstractPossessedMonster) m).ownClass) {
                case Glorious:
                    floors *= getNumGlories();
                    break;
                case Uncertain:
                    floors += getNumGlories() + getNumUncertainties();
                    break;
                case Risky:
                    floors += getNumGlories() + getNumUncertainties() + getNumRisks();
                    break;
                case Possessor:
                    floors *= getNumGlories() + getNumUncertainties() + getNumRisks();
            }
        }
        
        if (AbstractDungeon.ascensionLevel < 14) {
            floors += AbstractDungeon.ascensionLevel * 1.4F;
        }
        else if (AbstractDungeon.ascensionLevel <= 20) {
            floors += AbstractDungeon.ascensionLevel * 2.0F;
        }
        
        return Math.abs(MathUtils.ceil(floors / Math.abs(minHp - maxHp)));
    }
    
    private int getNumGlories() {
        int num = 0;
        if (player.masterDeck != null) {
            for (AbstractCard card : player.masterDeck.group) {
                if (card instanceof AbstractShiniesCard) {
                    num++;
                }
            }
        }
        return num;
    }

    private int getNumUncertainties() {
        int num = 0;
        if (player.masterDeck != null) {
            for (AbstractCard card : player.masterDeck.group) {
                if (card instanceof AbstractUncertaintiesCard) {
                    num++;
                }
            }
        }
        return num;
    }

    private int getNumRisks() {
        int num = 0;
        if (player.masterDeck != null) {
            for (AbstractCard card : player.masterDeck.group) {
                if (card instanceof AbstractRisksCard) {
                    num++;
                }
            }
        }
        return num;
    }
    
    public void summonSpectres(float x, float y) {
        RiskySpectre spectre = new RiskySpectre(68, 70, 4, AbstractDungeon.floorNum + 4, x, y);
        spectre.init();
        spectre.applyPowers();
        spectre.showHealthBar();
        spectre.usePreBattleAction();
        AbstractDungeon.getCurrRoom().monsters.addMonster(getSmartPosition(spectre), spectre);
    }
    
    private int getSmartPosition(AbstractMonster monster) {
        int index = 0;
        
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (monster.drawX > m.drawX)
                index++;
        }
        
        return index;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
        if (fireflyEffect || this.activeEffects.get(0)) {
            if (card.type == AbstractCard.CardType.SKILL 
                    && (card.costForTurn >= 2 && !card.freeToPlayOnce || card.cost == -1 && card.energyOnUse >= 2) 
                    && !card.cardID.equals(JarOfFireflies.ID)) {
                for (int i = 0; i < this.activeAmounts.get(0).intValue(); i++) {
                    this.addToBot(new VFXAction(new FireFlyEffect(Color.GOLD)));
                }
                this.addToBot(new HealAction(this.player, this.player, this.activeAmounts.get(0).intValue()));
            }
        }
        
        if (witchEffect || this.activeEffects.get(2)) {
            if (card.type == AbstractCard.CardType.ATTACK 
                    && (card.costForTurn >= 2 && !card.freeToPlayOnce || card.cost == -1 && card.energyOnUse >= 2)) {
                this.addToBot(new VFXAction(new LightBulbEffect(this.player.hb), 0.01F));
                this.addToBot(new DrawCardAction(this.player, this.activeAmounts.get(2).intValue(), false));
            }
        }
        updateEffectDescription();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        /*
        if (lostSoulEffect || this.activeEffects.get(3)) {
            if (!(card instanceof LostSoul)) {
                this.flash();
                LostSoul soul = new LostSoul();
                switch (card.rarity) {
                    case SPECIAL:
                    case BASIC:
                    case COMMON:
                        soul.setDefaults(AbstractDungeon.cardRandomRng.random(1, 2));
                        break;
                    case CURSE:
                    case RARE:
                    case UNCOMMON:
                        soul.setDefaults(AbstractDungeon.cardRandomRng.random(2, 3));
                        break;
                    default:
                        soul.setDefaults(3);
                        break;
                }
                this.addToBot(new MakeTempCardInDrawPileAction(soul, this.activeAmounts.get(3).intValue(), true, true));
            }
        }
        updateEffectDescription();
         */
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        try {
            //summonSpectres(150F, 200F);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVictory() {
        if (player.getRelic(ResurrectionStone.ID) != null) {
            player.getRelic(ResurrectionStone.ID).flash();
            AbstractDungeon.getCurrRoom().rewards.add(new MixesReward(4));
        } else {
            AbstractDungeon.getCurrRoom().rewards.add(new MixesReward());
        }
        
        if (isBookClub() || isGiftSets() || isLeviathans()) {
            AbstractDungeon.getCurrRoom().rewards.add(new PossessionsReward(isBookClub(), isGiftSets(), isLeviathans()));
        }
        
        
        if (this.activeAmounts.get(1) > 0.5D) {
            this.activeAmounts.set(1, this.activeAmounts.get(1) * 0.75D);
        }
        if (this.activeAmounts.get(1) < 0.1D) {
            this.activeAmounts.set(1, 0.1D);
        }
        this.activeEffects.set(2, false);
        this.activeAmounts.set(2, 0D);
        updateEffectDescription();
    }

    public void updateEffectDescription() {
        this.INFO = DESCRIPTIONS[0];

        if (this.activeEffects.get(0) || fireflyEffect) {
            this.INFO += DESCRIPTIONS[1] + this.activeAmounts.get(0).intValue() + DESCRIPTIONS[2];
        }
        if (this.activeEffects.get(1) || sunglassesEffect) {
            this.INFO += DESCRIPTIONS[3] + this.activeAmounts.get(1).floatValue();
        }
        if (this.activeEffects.get(2) || witchEffect) {
            this.INFO += DESCRIPTIONS[4] + this.activeAmounts.get(2).intValue() + DESCRIPTIONS[5];
        }
        if (this.activeEffects.get(3) || lostSoulEffect) {
            this.INFO += DESCRIPTIONS[6] + this.activeAmounts.get(3).intValue() + DESCRIPTIONS[7];
        }
        if (this.activeEffects.get(4) || nourishEffect) {
            this.INFO += DESCRIPTIONS[8];
        }
        
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.INFO));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return new PossessedManuscripts();
    }
    
    private boolean isBookClub() {
        if (player.masterDeck != null) {
            return player.masterDeck.findCardById(TheTerribleSecret.ID) != null && player.masterDeck.findCardById(CreationScience.ID) != null
                    && player.masterDeck.findCardById(BookOfDarkness.ID) != null && player.masterDeck.findCardById(BookClub.ID) == null;
        }
        return false;
    }

    private boolean isGiftSets() {
        if (player.masterDeck != null) {
            return player.masterDeck.findCardById(JarOfFireflies.ID) != null && player.masterDeck.findCardById(ToyExterminator.ID) != null
                    && player.masterDeck.findCardById(BrokenMagnet.ID) != null && player.masterDeck.findCardById(FashionableSunglasses.ID) != null
                    && player.masterDeck.findCardById(Gifts.ID) == null;
        }
        return false;
    }

    private boolean isLeviathans() {
        if (player.masterDeck != null) {
            return player.masterDeck.findCardById(Scavenger.ID) != null && player.masterDeck.findCardById(Stellarite.ID) != null
                    && player.masterDeck.findCardById(EtherDrake.ID) != null && player.masterDeck.findCardById(Leviathans.ID) == null;
        }
        return false;
    }

    @Override
    public ArrayList<ArrayList> onSave() {
        logger.info("===正在保存手稿的记录===");
        for (int i = 0; i < this.activeEffects.size(); i++) {
            logger.info("==获取Boolean组第" + i + "个成员：" + this.activeEffects.get(i));
        }
        for (int i = 0; i < this.activeAmounts.size(); i++) {
            logger.info("==获取Integer组第" + i + "个成员：" + this.activeAmounts.get(i));
            //logger.info("===获得Integer组第" + i + "个成员类型：" + this.activeAmounts.get(i).getClass().getName());
            //logger.info("对应的collections中的成员类型为：" + this.collections.get(1).get(i).getClass().getName());
        }
        return this.collections;
    }

    @Override
    public void onLoad(ArrayList<ArrayList> records) {
        if (records == null) {
            logger.info("===手稿记录丢失（有很大的可能是因为玩家……），请联系作者===");
            return;
        }
        
        logger.info("===正在抄写手稿的记录===");
        for (int i = 0; i < this.activeEffects.size(); i++) {
            logger.info("==Boolean组成员" + i + "：" + this.activeEffects.get(i) + "，目标值：" + records.get(0).get(i));
            this.activeEffects.set(i, (Boolean) records.get(0).get(i));
        }
        for (int i = 0; i < this.activeAmounts.size(); i++) {
            logger.info("==Integer组成员" + i + "：" + this.activeAmounts.get(i) + "，目标值：" + records.get(1).get(i));
            this.activeAmounts.set(i, (Double) records.get(1).get(i));
            //logger.info("===获得Integer组第" + i + "个成员类型：" + this.activeAmounts.get(i).getClass().getName());
            //logger.info("对应的records中的成员类型为：" + records.get(1).get(i).getClass().getName());
        }
        
        for (int i = 0; i < this.activeEffects.size(); i++) {
            logger.info("==Boolean组成员" + i + "：" + this.activeEffects.get(i) + "，最终值：" + records.get(0).get(i));
            if (this.activeEffects.get(i) != records.get(0).get(i))
                logger.info("===手稿Boolean记录出错！！！请通知作者===");
        }
        for (int i = 0; i < this.activeAmounts.size(); i++) {
            logger.info("==Integer组成员" + i + "：" + this.activeAmounts.get(i) + "，最终值：" + records.get(1).get(i));
            if (this.activeAmounts.get(i) != records.get(1).get(i))
                logger.info("===手稿Integer记录出错！！！请通知作者===");
        }
        
        updateEffectDescription();
    }
}
