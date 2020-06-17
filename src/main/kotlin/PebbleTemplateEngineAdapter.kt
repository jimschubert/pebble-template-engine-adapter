@file:JvmName("PebbleTemplateEngineAdapter")
package us.jimschubert.examples.engines

import org.openapitools.codegen.api.TemplatingGenerator
import java.io.StringWriter
import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import com.mitchellbosecke.pebble.loader.DelegatingLoader
import com.mitchellbosecke.pebble.loader.FileLoader
import org.openapitools.codegen.api.AbstractTemplatingEngineAdapter

class PebbleTemplateEngineAdapter : AbstractTemplatingEngineAdapter() {
    private val engine: PebbleEngine = PebbleEngine.Builder()
        .cacheActive(false)
        .loader(DelegatingLoader(listOf(FileLoader(), ClasspathLoader())))
        .build()

    /**
     * Provides an identifier used to load the adapter. This could be a name, uuid, or any other string.
     *
     * @return A string identifier.
     */
    override fun getIdentifier(): String = "pebble"

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
        val filePath = generator?.getFullTemplatePath(modifiedTemplate)

        val writer = StringWriter()
        engine.getTemplate(filePath?.toAbsolutePath().toString())?.evaluate(writer, bundle)
        return writer.toString()
    }

    /**
     * During generation, if a supporting file has a file extension that is
     * inside that array, then it is considered a templated supporting file
     * and we use the templating engine adapter to generate it
     * @return string array of the valid file extensions for this templating engine
     */
    override fun getFileExtensions(): Array<String> = arrayOf("pebble")
}