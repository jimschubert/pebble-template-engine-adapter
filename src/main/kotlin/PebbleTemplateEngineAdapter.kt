@file:JvmName("PebbleTemplateEngineAdapter")
package us.jimschubert.examples.engines

import org.openapitools.codegen.api.TemplatingEngineAdapter
import org.openapitools.codegen.api.TemplatingGenerator
import java.io.StringWriter
import java.io.Writer
import java.util.HashMap
import com.mitchellbosecke.pebble.template.PebbleTemplate
import com.mitchellbosecke.pebble.PebbleEngine
import org.openapitools.codegen.api.AbstractTemplatingEngineAdapter
import java.io.File
import java.nio.file.Files

class PebbleTemplateEngineAdapter : AbstractTemplatingEngineAdapter() {
    private val tempDir: File by lazy {
        val d = createTempDir("pebble-template-engine-adapter")
        d.deleteOnExit()
        d
    }
    private val engine: PebbleEngine = PebbleEngine.Builder().cacheActive(false).build()

    /**
     * Provides an identifier used to load the adapter. This could be a name, uuid, or any other string.
     *
     * @return A string identifier.
     */
    override fun getIdentifier(): String {
        return "pebble"
    }

    /**
     * Compiles a template into a string
     *
     * @param generator From where we can fetch the templates content (e.g. an instance of DefaultGenerator)
     * @param bundle The map of values to pass to the template
     * @param templateFile The name of the template (e.g. model.mustache )
     * @return the processed template result
     * @throws IOException an error ocurred in the template processing
     */
    override fun compileTemplate(
        generator: TemplatingGenerator?,
        bundle: MutableMap<String, Any>?,
        templateFile: String?
    ): String {
        val modifiedTemplate = this.getModifiedFileLocation(templateFile).first()
        println(modifiedTemplate)
        val file = createTempFile(modifiedTemplate, directory = tempDir)
        val contents = generator?.getFullTemplateContents(modifiedTemplate)?:""
        file.writeText(contents)
        file.deleteOnExit()
        val compiledTemplate: PebbleTemplate? = engine.getTemplate(file.absolutePath)
        val writer = StringWriter()
        compiledTemplate?.evaluate(writer, bundle)

        val output = writer.toString()
        return output
    }

    /**
     * During generation, if a supporting file has a file extension that is
     * inside that array, then it is considered a templated supporting file
     * and we use the templating engine adapter to generate it
     * @return string array of the valid file extensions for this templating engine
     */
    override fun getFileExtensions(): Array<String> {
        return arrayOf("pebble")
    }
}