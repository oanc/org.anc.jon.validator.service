package org.anc.json.validator.service

import org.anc.web.WebUtil
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.core.util.AsJson
import org.anc.json.compiler.SchemaCompiler
import org.anc.json.validator.Validator

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author Keith Suderman
 */
//@Path("/instance")
class InstanceValidator {

    // Cache any error messages generated while initializing the validators.
    String lifError
    String metadataError

    // Create separate validators for the interchange and metadata formats.
    Validator lifValidator
    Validator metadataValidator

    public InstanceValidator() {
        def handler = { message ->
            lifError = "LIF ERROR: $message"
        }
        lifValidator = getValidator('/lif.schema', handler)
        handler = { message ->
            metadataError = "ERROR: $message"
        }
        metadataValidator = getValidator('/metadata.schema', handler)
    }

    private Validator getValidator(String schemaName, Closure errorLogger) {
        URL resource = this.class.getResource(schemaName)
        if (resource == null) {
            errorLogger("Resource not found: " + schemaName)
            return null
        }
        String source = resource.text
        if (source == null) {
            errorLogger("Unable to read resource " + schemaName)
        }
        Validator validator = null
        try {
            validator = new Validator(new SchemaCompiler().compile(source))
        }
        catch (Exception e) {
            errorLogger(e.getMessage())
        }
        return validator
    }

    /**
     * Validates a LAPPS Interchange Format instance.
     */
    @POST
    @Path("/lif")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response validateLIF(String json) {
        if (lifValidator == null) {
            return WebUtil.error(lifError)
        }

        ProcessingReport report
        try {
            report = lifValidator.validate(json)
        }
        catch (Exception e) {
            return WebUtil.error(e.getMessage())
        }

        String message
        if (report.toList().size() > 0) {
            message = ((AsJson) report).asJson().toString()
        }
        else {
            return Response.noContent().build()
        }
        return WebUtil.json(message);
    }

    @POST
    @Path("/metadata")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response validateMetadata(String json) {
        if (metadataValidator == null) {
            return WebUtil.error(metadataError)
        }

        ProcessingReport report
        try {
            report = metadataValidator.validate(json)
        }
        catch (Exception e) {
            return WebUtil.error(e.message)
        }
        String message
        if (report.toList().size() > 0) {
            message = ((AsJson) report).asJson().toString()
        }
        else {
            //message = '{"status":"success"}'
            return Response.noContent().build()
        }
        return WebUtil.json(message)
    }
}
