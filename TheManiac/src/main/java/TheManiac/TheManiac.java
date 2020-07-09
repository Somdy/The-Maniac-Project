package TheManiac;

import TheManiac.cards.colorless.attack.Blades;
import TheManiac.cards.colorless.attack.DeusExMe;
import TheManiac.cards.colorless.skill.Perception;
import TheManiac.cards.colorless.skill.Unravel;
import TheManiac.cards.curses.*;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.cards.maniac_blue.attack.*;
import TheManiac.cards.maniac_blue.power.*;
import TheManiac.cards.maniac_blue.skill.*;
import TheManiac.cards.maniac_blue.weapon.*;
import TheManiac.character.TheManiacCharacter;
import TheManiac.helper.ManiacImageMaster;
import TheManiac.helper.ThePossessedCardSavior;
import TheManiac.helper.ThePossessedInitializer;
import TheManiac.minions.AbstractManiacMinion;
import TheManiac.monsters.enemies.*;
import TheManiac.patches.CardMarkFieldPatch;
import TheManiac.patches.NewRewardIType;
import TheManiac.relics.*;
import TheManiac.rewards.MixesReward;
import TheManiac.variables.EnchantNumber;
import TheManiac.variables.TheManiacExtraNumber;
import TheManiac.variables.TheManiacOtherNumber;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class TheManiac implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, 
        PostInitializeSubscriber, EditCharactersSubscriber, AddAudioSubscriber {
    public static final Logger logger = LogManager.getLogger(TheManiac.class.getName());
    public static final Color THE_MANIAC_BLUE = CardHelper.getColor(0,35,102);

    private static final String MODNAME = "The Maniac";
    private static final String MODID = "maniac";
    private static final String AUTHOR = "Somdy";
    private static final String DESCRIPTION = "Blahblahblahblah";
    public static Properties TheManiacSettings = new Properties();
    public static boolean leisureMode = false;
    public static boolean challengerMode = false;
    public static boolean talkiveMode = true;

    private static final String ATTACK_MANIAC_BLUE = "maniacMod/images/512defaults/cardui/maniac_attack.png";
    private static final String SKILL_MANIC_BLUE =  "maniacMod/images/512defaults/cardui/maniac_skill.png";
    private static final String POWER_MANIAC_BLUE = "maniacMod/images/512defaults/cardui/maniac_power.png";
    private static final String ENERGY_ORB_MANIAC_BLUE = "maniacMod/images/512defaults/cardui/cost_orb.png";
    private static final String CARD_ENERGY_ORB = "maniacMod/images/512defaults/cardui/small_orb.png";
    private static final String ATTACK_MANIAC_BLUE_PORTRAIT = "maniacMod/images/1024portraits/cardui/maniac_attack_1024.png";
    private static final String SKILL_MANIAC_BLUE_PORTRAIT = "maniacMod/images/1024portraits/cardui/maniac_skill_1024.png";
    private static final String POWER_MANIAC_BLUE_PORTRAIT = "maniacMod/images/1024portraits/cardui/maniac_power_1024.png";
    private static final String ENERGY_ORB_MANIAC_BLUE_PORTRAIT = "maniacMod/images/1024portraits/cardui/small_orb_1024.png";

    private static final String THE_MANIAC_BUTTON = "maniacMod/images/charSelect/button.png";
    private static final String THE_MANIAC_PORTRAIT = "maniacMod/images/charSelect/portrait.png";
    public static final String THE_MANIAC_SHOULDER_1 = "maniacMod/images/character/shoulder.png";
    public static final String THE_MANIAC_SHOULDER_2 = "maniacMod/images/character/shoulder2.png";
    public static final String THE_MANIAC_CORPSE = "maniacMod/images/character/corpse.png";
    public static final String THE_MANIAC_CHAR = "maniacMod/images/character/TheManiac.png";
    public static final String THE_MANIAC_SKELETON_ATLAS = "maniacMod/images/character/idle/skeleton.atlas";
    public static final String THE_MANIAC_SKELETON_JSON = "maniacMod/images/character/idle/skeleton.json";

    public static final String BADGE_IMAGE = "maniacMod/images/badge.png";

    @SuppressWarnings("unused")
    public static void initialize() { 
        logger.info("====正在初始化Maniac的基本数据");
        TheManiac TheManiac = new TheManiac();
        logger.info("Maniac的基本数据初始化成功====");
    }

    public TheManiac() {

        BaseMod.subscribe(this);

        BaseMod.addColor(TheManiacCharacter.Enums.MANIAC_BLUE, THE_MANIAC_BLUE, THE_MANIAC_BLUE, THE_MANIAC_BLUE, THE_MANIAC_BLUE,
                THE_MANIAC_BLUE, THE_MANIAC_BLUE, THE_MANIAC_BLUE, ATTACK_MANIAC_BLUE, SKILL_MANIC_BLUE, POWER_MANIAC_BLUE,
                ENERGY_ORB_MANIAC_BLUE, ATTACK_MANIAC_BLUE_PORTRAIT, SKILL_MANIAC_BLUE_PORTRAIT, POWER_MANIAC_BLUE_PORTRAIT,
                ENERGY_ORB_MANIAC_BLUE_PORTRAIT, CARD_ENERGY_ORB);
        
        BaseMod.addColor(TheManiacCharacter.Enums.MANIAC_POSSESSED, Color.BLACK.cpy(), Color.BLACK.cpy(), Color.BLACK.cpy(), Color.BLACK.cpy(),
                Color.BLACK.cpy(), Color.BLACK.cpy(), Color.BLACK.cpy(), ATTACK_MANIAC_BLUE, SKILL_MANIC_BLUE, POWER_MANIAC_BLUE,
                ENERGY_ORB_MANIAC_BLUE, ATTACK_MANIAC_BLUE_PORTRAIT, SKILL_MANIAC_BLUE_PORTRAIT, POWER_MANIAC_BLUE_PORTRAIT,
                ENERGY_ORB_MANIAC_BLUE_PORTRAIT, CARD_ENERGY_ORB);
        
        BaseMod.addSaveField("PossessedSavior", new ThePossessedCardSavior());
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("正在加载新角色: 狂徒");
        BaseMod.addCharacter(new TheManiacCharacter("The Maniac"),
                THE_MANIAC_BUTTON, THE_MANIAC_PORTRAIT, TheManiacCharacter.Enums.THE_MANIAC);
        logger.info("狂徒已加入日常暴毙队伍");
    }

    @Override
    public void receiveEditCards() {
        logger.info("===正在添加狂徒的初始卡牌");
        BaseMod.addCard(new Strike_Maniac());
        BaseMod.addCard(new Defend_Maniac());
        BaseMod.addCard(new BurnBlood());
        BaseMod.addCard(new AbyssShackle());
        logger.info("===正在添加狂徒的普通卡牌");
        BaseMod.addCard(new ListenToNone());
        BaseMod.addCard(new UndyingDesire());
        BaseMod.addCard(new SpectralKnives());
        BaseMod.addCard(new PanickyParry());
        BaseMod.addCard(new Intimidate());
        BaseMod.addCard(new MeleeStrike());
        BaseMod.addCard(new FindAShortcut());
        BaseMod.addCard(new DashForward());
        //BaseMod.addCard(new BackupWeapon());
        BaseMod.addCard(new BladeShoot());
        BaseMod.addCard(new ShelteringEdge());
        BaseMod.addCard(new BladesInCloak());
        BaseMod.addCard(new UntravelledPath());
        BaseMod.addCard(new WardAndDefend());
        BaseMod.addCard(new Confront());
        BaseMod.addCard(new Refine());
        logger.info("===正在添加狂徒的罕见卡牌");
        BaseMod.addCard(new ShiftingShadows());
        BaseMod.addCard(new WitnessTrueSorcery());
        BaseMod.addCard(new ColdHeart());
        BaseMod.addCard(new ManipulatedFaith());
        //BaseMod.addCard(new ImpaleFlesh());
        BaseMod.addCard(new Lacerate());
        BaseMod.addCard(new Whirlscythe());
        BaseMod.addCard(new GlassScratch());
        BaseMod.addCard(new SorceryHour());
        BaseMod.addCard(new Recollect());
        BaseMod.addCard(new Feint());
        BaseMod.addCard(new SecretBackups());
        BaseMod.addCard(new AllOutAttack());
        BaseMod.addCard(new ShatteredShield());
        BaseMod.addCard(new Infuriate());
        BaseMod.addCard(new LootBarrage());
        BaseMod.addCard(new PerceiveDanger());
        BaseMod.addCard(new FoilArmor());
        BaseMod.addCard(new ObscuredAttack());
        BaseMod.addCard(new Hasten());
        BaseMod.addCard(new AlleviatingSorcery());
        BaseMod.addCard(new HammerStrike());
        BaseMod.addCard(new EndOfTheRoad());
        BaseMod.addCard(new ReconstructWounds());
        BaseMod.addCard(new Lancinate());
        BaseMod.addCard(new Knockdown());
        BaseMod.addCard(new SeekerKnife());
        BaseMod.addCard(new Quickdraw());
        BaseMod.addCard(new ArmToTheTeeth());
        BaseMod.addCard(new Reforge());
        BaseMod.addCard(new Traceback());
        BaseMod.addCard(new Blastwave());
        //BaseMod.addCard(new FlyTheKnives());
        BaseMod.addCard(new Exterminator());
        BaseMod.addCard(new Retrospect());
        BaseMod.addCard(new SpectralFlash());
        BaseMod.addCard(new RallyToMe());
        BaseMod.addCard(new ExpectedMoves());
        //BaseMod.addCard(new DoubleEdge());
        BaseMod.addCard(new SeeingDetails());
        //BaseMod.addCard(new GremlinCompanion());
        BaseMod.addCard(new Precision());
        BaseMod.addCard(new BladeArtist());
        BaseMod.addCard(new Assassinate());
        logger.info("===正在添加狂徒的稀有卡牌");
        BaseMod.addCard(new TimeForm());
        BaseMod.addCard(new UnseenTerror());
        BaseMod.addCard(new TorturousScreams());
        BaseMod.addCard(new Collecting());
        //BaseMod.addCard(new WickedInstincts());
        BaseMod.addCard(new TwistedEddies());
        BaseMod.addCard(new Inhibition());
        BaseMod.addCard(new ExceedEnchantment());
        BaseMod.addCard(new FanTheHammer());
        BaseMod.addCard(new ShadowContact());
        BaseMod.addCard(new ReachVictory());
        BaseMod.addCard(new StormOfTime());
        BaseMod.addCard(new Simulacrum());
        BaseMod.addCard(new HammerDown());
        BaseMod.addCard(new Trap());
        BaseMod.addCard(new Deal());
        BaseMod.addCard(new UniversalPerception());
        logger.info("===正在添加无色卡牌");
        BaseMod.addCard(new Perception());
        BaseMod.addCard(new Blades());
        BaseMod.addCard(new DeusExMe());
        BaseMod.addCard(new Unravel());
        logger.info("===正在整理狂徒的武器库");
        /*
        BaseMod.addCard(new AncientSphere());
        BaseMod.addCard(new BigSword());
        BaseMod.addCard(new BitingDagger());
        BaseMod.addCard(new BurglaryToolkit());
        BaseMod.addCard(new CursedStaff());
        BaseMod.addCard(new DoomScythe());
        BaseMod.addCard(new DoomScytheAttack());
        BaseMod.addCard(new DualMachetes());
        BaseMod.addCard(new HeavySkull());
        BaseMod.addCard(new ScorchingSpear());
        */
        logger.info("===武器库早没了！！！");
        logger.info("===正在下咒");
        BaseMod.addCard(new Torture());
        BaseMod.addCard(new Humiliation());
        BaseMod.addCard(new Remorse());
        BaseMod.addCard(new Guilty());
        BaseMod.addCard(new Scruple());
        logger.info("===正在整理群魔的宝藏");
        ThePossessedInitializer.addCards();
        logger.info("所有新卡已添加！===");

        logger.info("===解锁所有新卡牌，仅供测试使用");
        UnlockTracker.unlockCard(Strike_Maniac.ID);
        UnlockTracker.unlockCard(Defend_Maniac.ID);
        UnlockTracker.unlockCard(BurnBlood.ID);
        UnlockTracker.unlockCard(AbyssShackle.ID);
        UnlockTracker.unlockCard(ListenToNone.ID);
        UnlockTracker.unlockCard(UndyingDesire.ID);
        UnlockTracker.unlockCard(SpectralKnives.ID);
        UnlockTracker.unlockCard(PanickyParry.ID);
        UnlockTracker.unlockCard(Intimidate.ID);
        UnlockTracker.unlockCard(MeleeStrike.ID);
        UnlockTracker.unlockCard(FindAShortcut.ID);
        UnlockTracker.unlockCard(DashForward.ID);
        //UnlockTracker.unlockCard(BackupWeapon.ID);
        UnlockTracker.unlockCard(BladeShoot.ID);
        UnlockTracker.unlockCard(ShelteringEdge.ID);
        UnlockTracker.unlockCard(BladesInCloak.ID);
        UnlockTracker.unlockCard(UntravelledPath.ID);
        UnlockTracker.unlockCard(WardAndDefend.ID);
        UnlockTracker.unlockCard(Confront.ID);
        UnlockTracker.unlockCard(Refine.ID);

        UnlockTracker.unlockCard(GremlinCompanion.ID);
        UnlockTracker.unlockCard(ShiftingShadows.ID);
        UnlockTracker.unlockCard(WitnessTrueSorcery.ID);
        UnlockTracker.unlockCard(ColdHeart.ID);
        UnlockTracker.unlockCard(ManipulatedFaith.ID);
        //UnlockTracker.unlockCard(ImpaleFlesh.ID);
        UnlockTracker.unlockCard(Lacerate.ID);
        UnlockTracker.unlockCard(Whirlscythe.ID);
        UnlockTracker.unlockCard(GlassScratch.ID);
        UnlockTracker.unlockCard(SorceryHour.ID);
        UnlockTracker.unlockCard(Recollect.ID);
        UnlockTracker.unlockCard(Feint.ID);
        UnlockTracker.unlockCard(SecretBackups.ID);
        UnlockTracker.unlockCard(AllOutAttack.ID);
        UnlockTracker.unlockCard(ShatteredShield.ID);
        UnlockTracker.unlockCard(Infuriate.ID);
        UnlockTracker.unlockCard(LootBarrage.ID);
        UnlockTracker.unlockCard(PerceiveDanger.ID);
        UnlockTracker.unlockCard(FoilArmor.ID);
        UnlockTracker.unlockCard(ObscuredAttack.ID);
        UnlockTracker.unlockCard(Hasten.ID);
        UnlockTracker.unlockCard(AlleviatingSorcery.ID);
        UnlockTracker.unlockCard(HammerStrike.ID);
        UnlockTracker.unlockCard(EndOfTheRoad.ID);
        UnlockTracker.unlockCard(ReconstructWounds.ID);
        UnlockTracker.unlockCard(Lancinate.ID);
        UnlockTracker.unlockCard(Knockdown.ID);
        UnlockTracker.unlockCard(SeekerKnife.ID);
        UnlockTracker.unlockCard(Quickdraw.ID);
        UnlockTracker.unlockCard(ArmToTheTeeth.ID);
        UnlockTracker.unlockCard(Reforge.ID);
        UnlockTracker.unlockCard(Traceback.ID);
        UnlockTracker.unlockCard(Blastwave.ID);
        //UnlockTracker.unlockCard(FlyTheKnives.ID);
        UnlockTracker.unlockCard(Exterminator.ID);
        UnlockTracker.unlockCard(Retrospect.ID);
        UnlockTracker.unlockCard(SpectralFlash.ID);
        UnlockTracker.unlockCard(RallyToMe.ID);
        UnlockTracker.unlockCard(ExpectedMoves.ID);
        //UnlockTracker.unlockCard(DoubleEdge.ID);
        UnlockTracker.unlockCard(SeeingDetails.ID);
        UnlockTracker.unlockCard(Precision.ID);
        UnlockTracker.unlockCard(BladeArtist.ID);
        UnlockTracker.unlockCard(Assassinate.ID);

        UnlockTracker.unlockCard(FanTheHammer.ID);
        UnlockTracker.unlockCard(TimeForm.ID);
        UnlockTracker.unlockCard(UnseenTerror.ID);
        UnlockTracker.unlockCard(TorturousScreams.ID);
        UnlockTracker.unlockCard(Collecting.ID);
        //UnlockTracker.unlockCard(WickedInstincts.ID);
        UnlockTracker.unlockCard(TwistedEddies.ID);
        UnlockTracker.unlockCard(Inhibition.ID);
        UnlockTracker.unlockCard(ExceedEnchantment.ID);
        UnlockTracker.unlockCard(ShadowContact.ID);
        UnlockTracker.unlockCard(ReachVictory.ID);
        UnlockTracker.unlockCard(StormOfTime.ID);
        UnlockTracker.unlockCard(Simulacrum.ID);
        UnlockTracker.unlockCard(HammerDown.ID);
        UnlockTracker.unlockCard(Trap.ID);
        UnlockTracker.unlockCard(UniversalPerception.ID);

        UnlockTracker.unlockCard(Perception.ID);
        UnlockTracker.unlockCard(Blades.ID);
        UnlockTracker.unlockCard(DeusExMe.ID);
        UnlockTracker.unlockCard(Unravel.ID);

        /*
        UnlockTracker.unlockCard(AncientSphere.ID);
        UnlockTracker.unlockCard(BigSword.ID);
        UnlockTracker.unlockCard(BitingDagger.ID);
        UnlockTracker.unlockCard(BurglaryToolkit.ID);
        UnlockTracker.unlockCard(CursedStaff.ID);
        UnlockTracker.unlockCard(DoomScythe.ID);
        UnlockTracker.unlockCard(DoomScytheAttack.ID);
        UnlockTracker.unlockCard(DualMachetes.ID);
        UnlockTracker.unlockCard(HeavySkull.ID);
        UnlockTracker.unlockCard(ScorchingSpear.ID);
        */

        UnlockTracker.unlockCard(Torture.ID);
        logger.info("已解锁所有新卡牌！===");

        logger.info("===正在添加新的变量");
        BaseMod.addDynamicVariable(new TheManiacExtraNumber());
        BaseMod.addDynamicVariable(new TheManiacOtherNumber());
        BaseMod.addDynamicVariable(new EnchantNumber());
        logger.info("已添加所有新变量！===");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("===正在添加新的遗物");
        BaseMod.addRelicToCustomPool(new DamagedAnvil(), TheManiacCharacter.Enums.MANIAC_BLUE);
        BaseMod.addRelicToCustomPool(new EnchantedAnvil(), TheManiacCharacter.Enums.MANIAC_BLUE);
        BaseMod.addRelicToCustomPool(new BrokenHorn(), TheManiacCharacter.Enums.MANIAC_BLUE);
        BaseMod.addRelicToCustomPool(new BetterBrokenHorn(), TheManiacCharacter.Enums.MANIAC_BLUE);
        BaseMod.addRelicToCustomPool(new MakerPen(), TheManiacCharacter.Enums.MANIAC_BLUE);
        BaseMod.addRelicToCustomPool(new VisionEye(), TheManiacCharacter.Enums.MANIAC_BLUE);
        BaseMod.addRelicToCustomPool(new SmartDetector(), TheManiacCharacter.Enums.MANIAC_BLUE);
        BaseMod.addRelic(new PossessedManuscripts(), RelicType.SHARED);
        BaseMod.addRelic(new ResurrectionStone(), RelicType.SHARED);
        BaseMod.addRelic(new EnigmaticDecoder(), RelicType.SHARED);
        BaseMod.addRelic(new EnigmaticEncoder(), RelicType.SHARED);
        logger.info("成功使用所有新遗物污染奖励池！===");
    }

    @Override
    public void receiveEditKeywords() {
        Settings.GameLanguage language = this.languageSupport();
        this.loadLocKeywords(Settings.GameLanguage.ENG);
        if (!language.equals(Settings.GameLanguage.ENG)) {
            this.loadLocKeywords(language);
        }
    }

    @Override
    public void receiveEditStrings() {
        String lang = getSupportedLanguage(Settings.language);
        logger.info("检测到语言：" + lang);
        BaseMod.loadCustomStringsFile(CardStrings.class, "maniacMod/localization/" + lang + "/TheManiac_cards.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "maniacMod/localization/" + lang + "/TheManiac_powers.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "maniacMod/localization/" + lang + "/CharacterStrings.json");
        BaseMod.loadCustomStringsFile(StanceStrings.class, "maniacMod/localization/" + lang + "/TheManiac_stances.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "maniacMod/localization/" + lang + "/TheManiac_relics.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "maniacMod/localization/" + lang + "/TheManiac_ui.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, "maniacMod/localization/" + lang + "/TheManiac_orbs.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "maniacMod/localization/" + lang + "/TheManiac_monsters.json");
    }

    private String getSupportedLanguage(Settings.GameLanguage language) {
        switch(language) {
            case ZHS:
                return "zhs";
            default:
                return "eng";
        }
    }

    public void loadLocKeywords(Settings.GameLanguage language) {
        Gson gson = new Gson();

        String languageString = "maniacMod/localization/" + getSupportedLanguage(Settings.language);
        String keywordStrings = Gdx.files.internal(languageString + "/TheManiac_keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            logger.info("添加关键字：" + v.NAMES[0]);
            BaseMod.addKeyword("maniac:", v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    private Settings.GameLanguage languageSupport() {
        switch(Settings.language) {
            case ZHS:
                return Settings.language;
            default:
                return Settings.GameLanguage.ENG;
        }
    }

    @Override
    public void receivePostInitialize() {
        logger.info("正在杀掉一些多余的马");
        Texture badge = new Texture(BADGE_IMAGE);
        ModPanel settings = new ModPanel();
        ModLabeledToggleButton talkiveModeButton = new ModLabeledToggleButton("健谈模式(Talkive Mode)", 350F, 700F, 
                Settings.BLUE_TEXT_COLOR, FontHelper.charDescFont, talkiveMode, settings, (label) -> {}, (button) -> {
            talkiveMode = button.enabled;
            try {
                SpireConfig config = new SpireConfig(MODNAME, "theManiacConfig", TheManiacSettings);
                config.setBool("TALKIVE", talkiveMode);
                config.save();
            } catch (Exception e) {
                logger.info("===Failed to make a talkive config===");
                e.printStackTrace();
            }
        });
        settings.addUIElement(talkiveModeButton);
        BaseMod.registerModBadge(badge, MODNAME, AUTHOR, DESCRIPTION, settings);
        ManiacImageMaster.Initialize();
        
        
        logger.info("==正在清扫未邀之地");

        BaseMod.addMonster(MonsterEncounterList.LOUSES_ENC, MutatedLouse.NAME, () -> new MonsterGroup(new AbstractMonster[] {
                new MutatedLouse(-350F, -6F), new VariantLouse(-123F, 0F), new MutatedLouse(120F, 6F)
        }));
        BaseMod.addMonster(MonsterEncounterList.BIRYD_ENC, Biryd.NAME, () -> new MonsterGroup(new AbstractMonster[] {
                new Biryd(-450F, 60F), new Biryd(-150F, 65F), new Biryd(155F, 55F)
        }));
        BaseMod.addMonster(MonsterEncounterList.JAWS_ENC, JawFlesh.NAME, () -> new MonsterGroup(new AbstractMonster[] {
                new JawFlesh(-440F, 2F), new JawFlesh(-220F, 4F), new JawFlesh(-20F, 2F)
        }));
        BaseMod.addMonster(MonsterEncounterList.THORNY_SHELL_ENC, ThornShellParasite.NAME, () -> new ThornShellParasite(-40F, 0F));
        BaseMod.addMonster(MonsterEncounterList.KAL_ENC, Kaleidoscrimson.NAME, () -> new Kaleidoscrimson(-55F, -2F));
        BaseMod.addMonster(MonsterEncounterList.PLANTZAR_ENC, Planzarre.NAME, () -> new Planzarre(-76F, 0F));
        BaseMod.addMonster(MonsterEncounterList.LAGAS_ENC, Lagavurple.NAME, () -> new MonsterGroup(new AbstractMonster[] {
                new Lagavulred(-260F, 0F), new Lagavurple(150F, 0F)
        }));
        BaseMod.addMonster(MonsterEncounterList.GHOST_ENC, wanderingGhost.NAME, () -> new MonsterGroup(new AbstractMonster[] {
                new TorchGhost(-510F, 15F), new TorchGhost(-365F, 15F), new wanderingGhost()
        }));
        BaseMod.addMonster(MonsterEncounterList.SNEC_ENC, Sneckouette.NAME, () -> new Sneckouette(-60F, 0F, false, false, -1));
        BaseMod.addMonster(MonsterEncounterList.MAW_ENC, Mawelling.NAME, () -> new Mawelling(-55F, 0F));
        BaseMod.addMonster(MonsterEncounterList.CORAD_ENC, Corruardian.NAME, () -> new Corruardian(-40F, 100F));
        BaseMod.addMonster(MonsterEncounterList.POSED_ENC, Possessed.NAME, () -> new Possessed(10F, 0F));
        
        BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.LOUSES_ENC, 6));
        BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.BIRYD_ENC, 6));
        BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.JAWS_ENC, 6));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.THORNY_SHELL_ENC, 10));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.KAL_ENC, 10));
        BaseMod.addStrongMonsterEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.POSED_ENC, 15));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.PLANTZAR_ENC, 12));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.LAGAS_ENC, 12));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(MonsterEncounterList.GHOST_ENC, 12));
        
        /*
        BaseMod.addBoss(Exordium.ID, MonsterEncounterList.SNEC_ENC, "maniacMod/images/map/bossIcon/sneckouette.png", "maniacMod/images/map/bossIcon/outline/sneckouette.png");
        BaseMod.addBoss(Exordium.ID, MonsterEncounterList.MAW_ENC, "maniacMod/images/map/bossIcon/mawelling.png", "maniacMod/images/map/bossIcon/outline/mawelling.png");
        BaseMod.addBoss(Exordium.ID, MonsterEncounterList.CORAD_ENC, "maniacMod/images/map/bossIcon/corruardian.png", "maniacMod/images/map/bossIcon/outline/corruardian.png");
        */
        logger.info("已将懦弱的敌人赶出未邀之地==");
        
        /*addCustomSound(makeID("CorruardianReboot_01"), "maniacMod/audio/sound/CorruardianRebootActivate_01.ogg");
        addCustomSound(makeID("CorruardianReboot_02"), "maniacMod/audio/sound/CorruardianRebootActivate_02.ogg");
        addCustomSound(makeID("ThePossessedDeath"), "maniacMod/audio/sound/ThePossessedDeath.ogg");
        addCustomSound(makeID("ApplyRestrainedSfx"), "maniacMod/audio/sound/RestrainedPowerApply_v1.ogg");
        addCustomSound(makeID("ManiacSelectSound"), "maniacMod/audio/sound/ManiacCharacterLoad_sound.ogg");*/
        
        //BaseMod.registerCustomReward(NewRewardIType.Possessed, rewardSave -> new MixesReward(), customReward -> new RewardSave(customReward.type.toString(), null));
    }
    
    public static String makeID(String ID) {
        return MODID + ":" + ID;
    }
    
    private static void addCustomSound(String ID, String path) {
        HashMap<String, Sfx> map = (HashMap)ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put(ID, new Sfx(path, false));
    }

    @Override
    public void receiveAddAudio() {
        logger.info("==正在演奏新的乐曲");
        BaseMod.addAudio(makeID("CorruardianReboot_01"), "maniacMod/audio/sound/CorruardianRebootActivate_01.ogg");
        BaseMod.addAudio(makeID("CorruardianReboot_02"), "maniacMod/audio/sound/CorruardianRebootActivate_02.ogg");
        BaseMod.addAudio(makeID("ThePossessedDeath"), "maniacMod/audio/sound/ThePossessedDeath.ogg");
        BaseMod.addAudio(makeID("ApplyRestrainedSfx"), "maniacMod/audio/sound/RestrainedPowerApply_v1.ogg");
        BaseMod.addAudio(makeID("ManiacSelectSound"), "maniacMod/audio/sound/ManiacCharacterLoad_sound.ogg");
        BaseMod.addAudio(makeID("ChooseRisks"), "maniacMod/audio/sound/RisksLaughEffect_v1.ogg");
        BaseMod.addAudio(makeID("ObtainUncertainties"), "maniacMod/audio/sound/RisksLaughEffect_v2.ogg");
        BaseMod.addAudio(makeID("ObtainShinies"), "maniacMod/audio/sound/RisksLaughEffect_v3.ogg");
        BaseMod.addAudio(makeID("ObtainRisks"), "maniacMod/audio/sound/RisksLaughEffect_v4.ogg");
        BaseMod.addAudio(makeID("ObtainPossessed"), "maniacMod/audio/sound/RisksLaughEffect_v5.ogg");
        BaseMod.addAudio(makeID("SpectreIgniteEffect_v1"), "maniacMod/audio/sound/SpectreIgniteEffect_v1.ogg");
        BaseMod.addAudio(makeID("SpectreIgniteEffect_v2"), "maniacMod/audio/sound/SpectreIgniteEffect_v2.ogg");
        BaseMod.addAudio(makeID("SpectreIgniteEffect_v3"), "maniacMod/audio/sound/SpectreIgniteEffect_v3.ogg");
        logger.info("演奏结束==");
    }
    
    public static void saveData() {
        
    }
}
