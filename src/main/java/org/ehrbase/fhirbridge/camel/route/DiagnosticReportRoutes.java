package org.ehrbase.fhirbridge.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators;
import org.springframework.stereotype.Component;

@Component
public class DiagnosticReportRoutes extends RouteBuilder {

    @Override
    public void configure() {
        // @formatter:off
        from("diagnostic-report-create:/service")
            .routeId("diagnosticReport-create")
            .setHeader(FhirCamelValidators.VALIDATION_MODE, constant(FhirCamelValidators.MODEL))
            .process(FhirCamelValidators.itiRequestValidator())
            .bean("myDiagnosticReportDaoR4", "create(${body})");

        from("diagnostic-report-read:/service")
            .routeId("diagnosticReport-read")
//            .to("ehr:aql:query")
            .process(exchange -> exchange.getMessage().setBody(new DiagnosticReport()))
            .to("log:debug?showAll=true");
        // @formatter:on
    }
}
