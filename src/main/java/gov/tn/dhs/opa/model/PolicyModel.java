package gov.tn.dhs.opa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("data-model")
    private DataModelLinks dataModelLinks;

    @JsonProperty("assessor")
    private AssessorLinks assessorLinks;

    @JsonProperty("links")
    private List<Link> links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataModelLinks getDataModelLinks() {
        return dataModelLinks;
    }

    public void setDataModelLinks(DataModelLinks dataModelLinks) {
        this.dataModelLinks = dataModelLinks;
    }

    public AssessorLinks getAssessorLinks() {
        return assessorLinks;
    }

    public void setAssessorLinks(AssessorLinks assessorLinks) {
        this.assessorLinks = assessorLinks;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
