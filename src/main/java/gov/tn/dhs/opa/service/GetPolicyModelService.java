package gov.tn.dhs.opa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.tn.dhs.opa.config.AppProperties;
import gov.tn.dhs.opa.model.GetPolicyModelRequest;
import gov.tn.dhs.opa.model.PolicyModel;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;

@Service
public class GetPolicyModelService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(GetPolicyModelService.class);

    public GetPolicyModelService(AppProperties appProperties) {
        super(appProperties);
    }

    public void process(Exchange exchange) {
        GetPolicyModelRequest getRoleRequest = exchange.getIn().getBody(GetPolicyModelRequest.class);
        String policyModelName = getRoleRequest.getPolicyModelName();
        if ((policyModelName == null) || policyModelName.trim().isEmpty()) {
            setupError("400", "No query parameter provided");
        }
        String requestUrl = appProperties.getBaseurl() + "/policy-models/" + policyModelName;
        String bearer = null;
        try {
            bearer = getBearer();
        } catch (IOException e) {
            setupError("500", "Authentication error");
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearer);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(requestUrl, HttpMethod.GET, request, String.class);
            String jsonString = response.getBody();
            logger.info("JSON response received: {}", jsonString);
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            PolicyModel policyModel = null;
            try {
                policyModel = objectMapper.readValue(jsonString, PolicyModel.class);
            } catch (JsonProcessingException e) {
                setupError("500", "Service error");
            }
            setupResponse(exchange, "200", policyModel);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                setupError("404", "Policy Model not found");
            } else {
                setupError("404", "Service error");
            }
        }
    }

}



