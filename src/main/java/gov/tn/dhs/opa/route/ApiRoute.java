package gov.tn.dhs.opa.route;

import com.fasterxml.jackson.core.JsonParseException;
import gov.tn.dhs.opa.config.AppProperties;
import gov.tn.dhs.opa.exception.ServiceErrorException;
import gov.tn.dhs.opa.model.GetPolicyModelRequest;
import gov.tn.dhs.opa.model.PolicyModel;
import gov.tn.dhs.opa.model.PolicyModels;
import gov.tn.dhs.opa.model.SimpleMessage;
import gov.tn.dhs.opa.service.GetPolicyModelService;
import gov.tn.dhs.opa.service.ListPolicyModelsService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Configuration
public class ApiRoute extends RouteBuilder {

    AppProperties appProperties;
    ListPolicyModelsService listPolicyModelsService;
    GetPolicyModelService getPolicyModelService;

    public ApiRoute(
            ListPolicyModelsService listPolicyModelsService,
            GetPolicyModelService getPolicyModelService,
            AppProperties appProperties) {
        this.listPolicyModelsService = listPolicyModelsService;
        this.getPolicyModelService = getPolicyModelService;
        this.appProperties = appProperties;
    }

    @Override
    public void configure() {

        onException(JsonParseException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(constant("{}"))
        ;

        onException(Exception.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(simple("${exception.message}"))
        ;

        onException(ServiceErrorException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("${exception.code}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(simple("${exception.message}"))
        ;

        restConfiguration()
                .enableCORS(true)
                .apiProperty("cors", "true") // cross-site
                .component("servlet")
                .port(appProperties.getServerPort())
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest()
                .post("/list_policy_models")
                .outType(PolicyModels.class)
                .to("direct:listPolicyModelsService")
        ;
        from("direct:listPolicyModelsService")
                .log("received request to list all policy models ...")
                .bean(listPolicyModelsService)
                .endRest()
        ;

        rest()
                .post("/get_policy_model")
                .type(GetPolicyModelRequest.class)
                .outType(PolicyModel.class)
                .to("direct:RoleService")
        ;
        from("direct:RoleService")
                .log("received request to get role ...")
                .bean(getPolicyModelService)
                .endRest()
        ;

        rest()
                .get("/")
                .to("direct:runningStatus")
        ;
        from("direct:runningStatus")
                .log("runStatus property value is " + appProperties.getRunStatus())
                .process(exchange -> exchange.getIn().setBody(new SimpleMessage(appProperties.getRunStatus()), SimpleMessage.class))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader("Content-Type", constant("application/json"))
                .endRest()
        ;

    }

}
