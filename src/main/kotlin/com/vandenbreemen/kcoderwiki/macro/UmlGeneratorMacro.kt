package com.vandenbreemen.kcoderwiki.macro

import com.vandenbreemen.grucd.main.Main
import com.vandenbreemen.ktt.interactor.StaticContentInteractor
import com.vandenbreemen.ktt.interactor.SystemAccessInteractor
import com.vandenbreemen.ktt.macro.Macro
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class UmlGeneratorMacro(private val staticContentInteractor: StaticContentInteractor,

    ): Macro {

    override val name: String
        get() = "umlGen"


    val usage = "Usage:  path=/path/to/code"

    override val description: String?
        get() = """
            Generates UML for the code in a specified path.
            ##### Specific Usage:
            $usage
        """.trimIndent()

    private val jobSet: MutableSet<String> = ConcurrentHashMap.newKeySet()
    private val pathsToGeneratedUml = ConcurrentHashMap<String, String>()

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

                    pathsToGeneratedUml[path] = result

                    jobSet.remove(path)
                }
            }


            return """
                    ## UML for $path
                    
                    ${pathsToGeneratedUml[path]}
                    
                    Note that ${if(alreadyRunning) "there is already a diagram in progress for this code" else "Diagramming has just been started"}  
                """.trimIndent()

        } ?: run {
            return usage
        }

    }


}