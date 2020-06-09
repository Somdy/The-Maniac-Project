package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRisksCard extends ManiacRisksCard {
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("maniac:RisksCard");
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public List<TooltipInfo> tips = new ArrayList<>();
    public boolean isThrilled;
    
    public AbstractRisksCard(String id, String img, int cost, CardType type, CardTarget target, TooltipInfo info) {
        super(id, img, cost, type, Enum.THE_RISKS, target);
        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
        this.tips.add(info);
        this.isThrilled = false;
    }

    public AbstractRisksCard(String id, String img, int cost, CardType type, CardTarget target) {
        super(id, img, cost, type, Enum.THE_RISKS, target);
        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
        this.isThrilled = false;
    }

    @Override
    public void setOutlook() {
        this.setOrbTexture("maniacMod/images/512defaults/cardui/card_risks_orb.png",
                "maniacMod/images/1024portraits/cardui/risks_orb.png");
        this.setBannerTexture("maniacMod/images/512defaults/cardui/banner_risks.png", 
                "maniacMod/images/1024portraits/cardui/banner_risks.png");
        switch (this.type) {
            case ATTACK:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_attack_risks.png",
                        "maniacMod/images/1024portraits/cardui/bg_attack_risks.png");
                this.setPortraitTextures("maniacMod/images/512defaults/cardui/frame_attack_risks.png", 
                        "maniacMod/images/1024portraits/cardui/frame_attack_risks.png");
                break;
            case SKILL:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_skill_risks.png",
                        "maniacMod/images/1024portraits/cardui/bg_skill_risks.png");
                this.setPortraitTextures("maniacMod/images/512defaults/cardui/frame_skill_risks.png",
                        "maniacMod/images/1024portraits/cardui/frame_skill_risks.png");
                break;
            default:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_power.png",
                        "maniacMod/images/1024portraits/cardui/bg_power.png");

        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        if (isThrilled)
            thrill(p, m, true);
    }

    public abstract void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse);

    @Override
    public void onObtain() {
        super.onObtain();
        CardCrawlGame.sound.play(TheManiac.makeID("ObtainRisks"));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (isThrilled)
            this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
        return this.tips;
    }

    @Override
    public void onVictoryCombat(boolean inDeck) {
        super.onVictoryCombat(inDeck);
        if (isThrilled)
            this.isThrilled = false;
    }
}
