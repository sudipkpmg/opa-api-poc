package gov.tn.dhs.opa.model;

import java.util.List;

public class ExampleRequest {

    List<String> outcomes;
    List<ExampleCase> cases;

    public List<String> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<String> outcomes) {
        this.outcomes = outcomes;
    }

    public List<ExampleCase> getCases() {
        return cases;
    }

    public void setCases(List<ExampleCase> cases) {
        this.cases = cases;
    }

}
