package io.gbell.models.tv;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Data")
public class TVShowResponse {

    @Element(name = "Series")
    private TVShow showDetails;

    @ElementList(name = "Episode", inline = true, required = false)
    private List<TVEpisode> episodes;

    public TVShowResponse() {}

    public TVShow getShowDetails() {
        return showDetails;
    }

    public List<TVEpisode> getEpisodes() {
        return episodes;
    }
}
