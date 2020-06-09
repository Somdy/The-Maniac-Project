package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUncertaintiesCard extends ManiacRisksCard {
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("maniac:UncertaintiesCard");
    public List<TooltipInfo> tips = new ArrayList<>();
    
    public AbstractUncertaintiesCard(String id, String img, int cost, CardType type, CardTarget target, TooltipInfo info) {
        super(id, img, cost, type, Enum.THE_UNCERTAINTIES, target);
        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
        this.tips.add(info);
    }

    public AbstractUncertaintiesCard(String id, String img, int cost, CardType type, CardTarget target) {
        super(id, img, cost, type, Enum.THE_UNCERTAINTIES, target);
        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
    }

    @Override
    public void setOutlook() {
        this.setOrbTexture("maniacMod/images/512defaults/cardui/card_uncertainties_orb.png",
                "maniacMod/images/1024portraits/cardui/uncertainties_orb.png");
        this.setBannerTexture("maniacMod/images/512defaults/cardui/banner_uncertainties.png",
                "maniacMod/images/1024portraits/cardui/banner_uncertainties.png");
        switch (this.type) {
            case ATTACK:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_attack_uncertainties.png",
                        "maniacMod/images/1024portraits/cardui/bg_attack_uncertainties.png");
                this.setPortraitTextures("maniacMod/images/512defaults/cardui/frame_attack_uncertainties.png",
                        "maniacMod/images/1024portraits/cardui/frame_attack_uncertainties.png");
                break;
            case SKILL:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_skill_uncertainties.png",
                        "maniacMod/images/1024portraits/cardui/bg_skill_uncertainties.png");
                this.setPortraitTextures("maniacMod/images/512defaults/cardui/frame_skill_uncertainties.png",
                        "maniacMod/images/1024portraits/cardui/frame_skill_uncertainties.png");
                break;
            default:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_power.png",
                        "maniacMod/images/1024portraits/cardui/bg_power.png");

        }
    }

    @Override
    public void onObtain() {
        super.onObtain();
        CardCrawlGame.sound.play(TheManiac.makeID("ObtainUncertainties"));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }
}
