package org.ehrbase.fhirbridge.camel;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.apache.camel.builder.RouteBuilder;
import org.ehrbase.client.aql.query.Query;
import org.ehrbase.fhirbridge.camel.component.ehr.aql.AqlConstants;
import org.ehrbase.fhirbridge.camel.processor.DefaultCreateResourceRequestValidator;
import org.ehrbase.fhirbridge.camel.processor.PatientIdProcessor;
import org.ehrbase.fhirbridge.ehr.converter.DiagnoseCompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.diagnosecomposition.DiagnoseComposition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Condition;
import org.springframework.stereotype.Component;

@Component
public class ConditionRoutes extends RouteBuilder {

    private final IFhirResourceDao<Condition> conditionDao;

    private final DefaultCreateResourceRequestValidator requestValidator;

    private final PatientIdProcessor patientIdProcessor;

    public ConditionRoutes(IFhirResourceDao<Condition> conditionDao,
                           DefaultCreateResourceRequestValidator requestValidator,
                           PatientIdProcessor patientIdProcessor) {
        this.conditionDao = conditionDao;
        this.requestValidator = requestValidator;
        this.patientIdProcessor = patientIdProcessor;
    }

    @Override
    public void configure() {
        // @formatter:off
        from("cond-create:/service?audit=false&fhirContext=#fhirContext")
            .routeId("fhir:create-condition")
            .process(requestValidator)
            .bean(conditionDao, "create(${body})")
            .setBody(simple("${body.resource}"))
            .process(patientIdProcessor)
            .to("ehr-composition:/test?operation=mergeCompositionEntity&converter=#diagnoseCompositionConverter")
            .setBody(exchange -> new MethodOutcome().setResource(exchange.getIn().getBody(IBaseResource.class)));

//        from("cond-find:/service&fhirContext=#fhirContext")
//            .setHeader(AqlConstants.QUERY, constant(Query.buildNativeQuery(
//                "SELECT c " +
//                    "FROM EHR e CONTAINS COMPOSITION c " +
//                    "WHERE c/archetype_details/template_id/value = 'Diagnose' " +
//                    "AND c/uid/value = $compositionId", DiagnoseComposition.class)))
//            .to("ehr-aql:/myService?singleResult=true")
//            .bean(DiagnoseCompositionConverter.class, "fromComposition(${body.value1})");

//        from("direct:aql-demo-route")
//            .setHeader(AqlConstants.QUERY, constant(Query.buildNativeQuery(
//            "SELECT c " +
//                "FROM EHR e CONTAINS COMPOSITION c " +
//                "WHERE c/archetype_details/template_id/value = 'Diagnose' " +
//                "AND c/uid/value = $compositionId", DiagnoseComposition.class)))
//            .to("ehr-aql:/myService?singleResult=true")
//            .setBody(simple("${body.value1}"));
        // @formatter:on
    }
}
