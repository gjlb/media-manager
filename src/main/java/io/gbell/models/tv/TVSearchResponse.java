package io.gbell.models.tv;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Data")
public class TVSearchResponse {

    @ElementList(name = "Series", inline = true, required = false)
    private List<TVSearchResult> searchResults;

    public TVSearchResponse() {}

    public List<TVSearchResult> getSearchResults() {
        return searchResults;
    }
}
