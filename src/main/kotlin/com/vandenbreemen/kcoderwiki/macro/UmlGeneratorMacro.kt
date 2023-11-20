package com.vandenbreemen.kcoderwiki.macro

import com.vandenbreemen.grucd.builder.SourceCodeExtractor
import com.vandenbreemen.grucd.main.Main
import com.vandenbreemen.grucd.render.plantuml.PlantUMLRenderer
import com.vandenbreemen.grucd.render.plantuml.PlantUMLScriptGenerator
import com.vandenbreemen.ktt.interactor.StaticContentInteractor
import com.vandenbreemen.ktt.interactor.SystemAccessInteractor
import com.vandenbreemen.ktt.macro.Macro
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.time.Duration.Companion.hours

class UmlGeneratorMacro(private val staticContentInteractor: StaticContentInteractor,

    ): Macro {

    override val name: String
        get() = "umlGen"


    private val usage = "Usage:  path=/path/to/code, annotations=MyAnnotation"

    override val description: String?
        get() = """
            Generates UML for the code in a specified path.
            ##### Specific Usage:
            $usage
            
            Note that the annotations item is optional.  Annotation names must be separated by spaces
        """.trimIndent()

    private val jobSet: MutableSet<String> = ConcurrentHashMap.newKeySet()

    private val pathsToGeneratedUml = Cache.Builder<String, String>().expireAfterAccess(
        8.hours
    ).build()

    override fun execute(args: Map<String, String>, dataAccessInteractor: SystemAccessInteractor): String {

        args["path"] ?.let { path->

            val annotationsRaw = args["annotations"]?.split(" ")

            var alreadyRunning = false
            if(jobSet.contains(path)) {
                alreadyRunning = true
            } else  {
                jobSet.add(path)
                CoroutineScope(Dispatchers.IO).launch {

                    val extractor = SourceCodeExtractor()
                    annotationsRaw?.let { annotationNames->
                        annotationNames.forEach { name->extractor.filterForAnnotationType(name) }
                    }

                    val fileList = extractor.getFilenamesToVisit(null, path)
                    val model = extractor.buildModelWithFiles(fileList)
                    val script = PlantUMLScriptGenerator().render(model)
                    val plantUmlRenderer = PlantUMLRenderer()
                    val svg = plantUmlRenderer.renderSVG(script)

                    pathsToGeneratedUml.put(path, svg)

                    jobSet.remove(path)
                }
            }


            return """
                    ## UML for $path
                    
                    ${pathsToGeneratedUml.get(path) ?: "Not available...."}
                    
                    Note that ${if(alreadyRunning) "there is already a diagram in progress for this code" else "Diagramming has just been started"}  
                """.trimIndent()

        } ?: run {
            return usage
        }

    }


}