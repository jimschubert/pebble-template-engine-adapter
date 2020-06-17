# Pebble Template Engine Adapter

This project demonstrates how to use new experimental custom template engine support introduced in OpenAPI Generator 4.0.0.

The example templates provided here are minimal (as a full working template is irrelevant to the example) and do not result in functional code.

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

You'll need to reference the OpenAPI Generator CLI jar directly for the following. You can [download version 4.3.1 of the CLI directly](https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/4.3.1/openapi-generator-cli-4.3.1.jar) or run the cli.sh script in this repository's root to cache locally (BONUS! you can later use this CLI script):

```bash
OPENAPI_GENERATOR_VERSION=4.3.1 ./cli.sh help
```

Next, compile a sample generator with the minimal templates from above:

```
java $JAVA_OPTS -cp "${PWD}"/build/libs/pebble-template-engine-adapter-1.0-SNAPSHOT-all.jar:"${PWD}"/openapi-generator-cli-4.3.1.jar \
    org.openapitools.codegen.OpenAPIGenerator \
    generate \
    -g go \
    -i ${YOUR_OPENAPI_DOC} \
    -e us.jimschubert.examples.engines.PebbleTemplateEngineAdapter \
    -o /tmp/pebble-example/out \
    -t /tmp/pebble-example/templates \
    -Dmodels -DmodelDocs=false -DmodelTests=false -Dapis -DapiTests=false -DapiDocs=false
```

For reproducible command, you can evaluate the above with

```bash
export YOUR_OPENAPI_DOC=https://raw.githubusercontent.com/OpenAPITools/openapi-generator/53eff431848291f294cdbd7941fe7ff8dedbaea2/modules/openapi-generator/src/test/resources/3_0/petstore.yaml
```

Now go check out your generated code at `/tmp/pebble-example/out/` which used your custom Pebble templates!

Refer to https://pebbletemplates.io for more details about this templating syntax.
