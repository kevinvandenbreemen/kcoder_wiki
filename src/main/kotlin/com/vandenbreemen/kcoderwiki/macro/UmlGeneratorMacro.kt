package com.vandenbreemen.kcoderwiki.macro

import com.vandenbreemen.grucd.main.Main
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


    private val usage = "Usage:  path=/path/to/code"

    override val description: String?
        get() = """
            Generates UML for the code in a specified path.
            ##### Specific Usage:
            $usage
        """.trimIndent()

    private val jobSet: MutableSet<String> = ConcurrentHashMap.newKeySet()

    private val pathsToGeneratedUml = Cache.Builder<String, String>().expireAfterAccess(
        8.hours
    ).build()

    override fun execute(args: Map<String, String>, dataAccessInteractor: SystemAccessInteractor): String {

        args["path"] ?.let { path->

            var alreadyRunning = false
            if(jobSet.contains(path)) {
                alreadyRunning = true
            } else  {
                jobSet.add(path)
                CoroutineScope(Dispatchers.IO).launch {
                    val result = Main.generateAndProcessUML(arrayOf(
                        "-d", path
                    ))

                    pathsToGeneratedUml.put(path, result)

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