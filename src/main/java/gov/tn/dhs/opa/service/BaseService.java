package gov.tn.dhs.opa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.tn.dhs.opa.config.AppProperties;
import gov.tn.dhs.opa.exception.ServiceErrorException;
import gov.tn.dhs.opa.model.ServiceError;
import gov.tn.dhs.opa.model.SimpleMessage;
import gov.tn.dhs.opa.util.JsonUtil;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public abstract class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    protected final AppProperties appProperties;

    public BaseService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    protected abstract void process(Exchange exchange) throws Exception;

    protected void setupResponse(Exchange exchange, String code, Object response) {
        exchange.getIn().setBody(response, response.getClass());
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/json");
        exchange.getIn().setHeader("Accept", "application/json");
    }

    protected void setupResponse(Exchange exchange, String code, Object response, Class clazz) {
        exchange.getIn().setBody(response, clazz);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/json");
        exchange.getIn().setHeader("Accept", "application/json");
    }

    protected void setupOctetStreamResponse(Exchange exchange, String code, byte[] data) {
        exchange.getIn().setBody(data, byte[].class);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/octet-stream");
    }

    protected void setupMessage(Exchange exchange, String code, String message) {
        SimpleMessage simpleMessage = new SimpleMessage(message);
        exchange.getIn().setBody(simpleMessage, SimpleMessage.class);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code);
        exchange.getIn().setHeader("Content-Type", "application/json");
    }

    protected void setupError(String code, String message) {
        ServiceError clientError = new ServiceError(code, message);
        throw new ServiceErrorException(code, JsonUtil.toJson(clientError));
    }

    protected JsonNode getJsonFromResponse(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode;
    }

    protected String getBearer() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", "devapi");
        map.add("client_secret", "976DyY#q");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(appProperties.getBearerTokenUrl(), request , String.class);
        String jsonString = response.getBody();
        JsonNode jsonNode = getJsonFromResponse(jsonString);
        String accessToken = jsonNode.get("access_token").asText();
        String bearer = "Bearer " + accessToken;
        return bearer;
    }

}
