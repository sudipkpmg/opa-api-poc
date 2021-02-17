package gov.tn.dhs.opa.model;

import java.util.List;

public class ExampleResponse {

    List<ExampleCase> cases;
    List<ExampleSummary> summary;

    public List<ExampleCase> getCases() {
        return cases;
    }

    public void setCases(List<ExampleCase> cases) {
        this.cases = cases;
    }

    public List<ExampleSummary> getSummary() {
        return summary;
    }

    public void setSummary(List<ExampleSummary> summary) {
        this.summary = summary;
    }

}
