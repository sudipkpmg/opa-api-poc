package gov.tn.dhs.opa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.tn.dhs.opa.config.AppProperties;
import gov.tn.dhs.opa.model.PolicyModels;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ListPolicyModelsService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ListPolicyModelsService.class);

    public ListPolicyModelsService(AppProperties appProperties) {
        super(appProperties);
    }

    public void process(Exchange exchange) {
        String requestUrl = appProperties.getBaseurl() + "/policy-models";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String bearer = getBearerWithRetry();
        headers.set("Authorization", bearer);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, request, String.class);
        int statusCode = response.getStatusCodeValue();
        if (statusCode == 200) {
            String jsonString = response.getBody();
            logger.info("JSON response received: {}", jsonString);
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            PolicyModels policyModels = null;
            try {
                policyModels = objectMapper.readValue(jsonString, PolicyModels.class);
            } catch (JsonProcessingException e) {
                setupError("500", "Service error");
            }
            setupResponse(exchange, "200", policyModels);
        } else {
            if (statusCode == 404) {
                setupError("404", "No role found");
            } else {
                setupError("500", "Service error");
            }
        }
    }

}



