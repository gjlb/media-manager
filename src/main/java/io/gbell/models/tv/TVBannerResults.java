package io.gbell.models.tv;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Banners")
public class TVBannerResults {

    @ElementList(name = "Banner", inline = true, required = false)
    private List<TVShowBanner> bannerResults;

    public TVBannerResults() {}

    public List<TVShowBanner> getBannerResults() {
        return bannerResults;
    }
}
