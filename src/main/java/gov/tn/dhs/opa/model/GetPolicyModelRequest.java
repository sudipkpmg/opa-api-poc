package gov.tn.dhs.opa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPolicyModelRequest {

    @JsonProperty("policy-model-name")
    private String policyModelName;

    public String getPolicyModelName() {
        return policyModelName;
    }

    public void setPolicyModelName(String policyModelName) {
        this.policyModelName = policyModelName;
    }

}
