package io.gbell.models.tv;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Series", strict = false)
public class TVSearchResult {

    @Element(name = "id")
    private int id;

    @Element(name = "SeriesName")
    private String title;

    @Element(name = "language")
    private String language;

    @Element(name = "Overview", required = false)
    private String synopsis;

    @Element(name = "FirstAired", required = false)
    private String startedAiringDate;

    public TVSearchResult() {}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getStartedAiringDate() {
        return startedAiringDate;
    }
}
