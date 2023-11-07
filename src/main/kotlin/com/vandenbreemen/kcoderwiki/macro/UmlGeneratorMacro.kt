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


    val usage = "Usage:  path=/path/to/code, fileName=image.png"

    override val description: String?
        get() = """
            Generates UML for the code in a specified path.
            ##### Specific Usage:
            $usage
        """.trimIndent()

    private val jobSet: MutableSet<String> = ConcurrentHashMap.newKeySet()

    override fun execute(args: Map<String, String>, dataAccessInteractor: SystemAccessInteractor): String {

        args["path"] ?.let { path->

            args["fileName"]?.let { ouputFile->

                var alreadyRunning = false
                if(jobSet.contains(path)) {
                    println("RUNNING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    alreadyRunning = true
                } else  {
                    jobSet.add(path)
                    CoroutineScope(Dispatchers.IO).launch {
                        Main.main(arrayOf(
                            "-o", "${staticContentInteractor.staticContentRoot}/$ouputFile", "-d", path
                        ))
                        jobSet.remove(path)
                    }
                }


                return """
                    ## UML for $path
                    
                    ![UML for $path](/res/$ouputFile)
                    
                    Note that ${if(alreadyRunning) "there is already a diagram in progress for this code" else "Diagramming has just been started"}  
                """.trimIndent()

            } ?: run {
                return usage
            }

        } ?: run {
            return usage
        }

    }


}