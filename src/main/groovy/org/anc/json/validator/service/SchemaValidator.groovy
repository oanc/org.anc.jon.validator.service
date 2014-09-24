package org.anc.json.validator.service

import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.core.util.AsJson
import org.anc.web.WebUtil

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author Keith Suderman
 */
@Path("/schema")
class SchemaValidator {

    org.anc.json.validator.SchemaValidator validator = new org.anc.json.validator.SchemaValidator()

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes([MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN])
    Response jsonSchema(String schema) {
        String json = '{"status":"error","type":"unknown"}'
        try {
            ProcessingReport report = validator.validate(schema)
            if (!report.iterator().hasNext()) {
                //json = '{"status":"success"}'
                return Response.noContent().build()
            }
            else {
                json = ((AsJson) report).asJson().toString()
            }
        }
        catch (Exception e) {
            e.printStackTrace()
            //json = '{"status":"error","type":"' + e.message + '"}'
            return WebUtil.error(e.message)
        }
        return WebUtil.json(json)
    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.TEXT_PLAIN)
//    Response groovySchema(String schema) {
//        ProcessingReport report = validator.validate(schema)
//        String json = ((AsJson)report).asJson().toString()
//        return WebUtil.json(json)
//    }
}
