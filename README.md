org.anc.jon.validator.service
=============================

Web service for validating JSON schema in either LAX or Javascript syntax.

## Usage

The service exposes three URL that users can POST data to:

1. **http://&lt;server>:&lt;port>/json-validator/lif** <br/>Validates LIF (LAPPS Interchange Format) JSON instance using the
schema at [http://vocab.lappsgrid.org/schema/lif.schema](http://vocab.lappsgrid.org/schema/lif.schema)
1. **http://&lt;server>:&lt;port>/json-validator/metadata** <br/>Validates service metadata using the
schema at [http://vocab.lappsgrid.org/schema/metadata.schema](http://vocab.lappsgrid.org/schema/metadata.schema)
1. **http://&lt;server>:&lt;port>/json-validator/schema** <br/>Validates a JSON schema using the 
Draft 4 specification.  The schema may use with the LAX or Javascript syntax.

For more information on the LAX Schema syntax see [http://github.com/oanc/org.anc.json.schema-compiler](http://github.com/oanc/org.anc.json.schema-compiler)