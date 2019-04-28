# Pebble Template Engine Adapter

This project demonstrates how to use new experimental custom template engine support in OpenAPI Generator 4.0.0.
The example templates provided here are minimal (as a full working template is irrelevant to the example).

First, compile a fatjar of this project.

```
./gradlew shadowJar
```

Then, create a template directory:

```
mkdir -p /tmp/pebble-example/templates
```

And put the following two templates in that directory:

api.pebble
```
package {{packageName}}

import (
    "net/http"
{% for item in imports %}
    "{{item.import}}"
{% endfor %}
)

type Generated{{classname}}Servicer 

// etc

```

model.pebble
```
package {{packageName}}
{% for model in models %}
Don't generate any model information :)
{% endfor %}
```

Next, compile a sample generator with the minimal templates from above:

```
java $JAVA_OPTS -cp /path/to/pebble-template-engine-adapter-1.0-SNAPSHOT-all.jar:/path/to/openapi-generator-cli.jar \
    org.openapitools.codegen.OpenAPIGenerator \
    generate \
    -g go \
    -i ${YOUR_OPENAPI_DOC} \
    -e us.jimschubert.examples.engines.PebbleTemplateEngineAdapter \
    -o /tmp/pebble-example/out \
    -t /tmp/pebble-example/templates \
    -Dmodels -DmodelDocs=false -DmodelTests=false -Dapis -DapiTests=false -DapiDocs=false
```

Refer to https://pebbletemplates.io for more details about this templating syntax.
