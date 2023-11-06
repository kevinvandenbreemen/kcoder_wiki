package com.vandenbreemen.kcoderwiki.macro

import com.vandenbreemen.grucd.main.Main
import com.vandenbreemen.ktt.interactor.StaticContentInteractor
import com.vandenbreemen.ktt.macro.Macro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UmlGeneratorMacro(private val staticContentInteractor: StaticContentInteractor,

    ): Macro {

    override val name: String
        get() = "umlGen"

    val usage = "Usage:  path=/path/to/code, fileName=image.png"

    override fun execute(args: Map<String, String>): String {



        CoroutineScope(Dispatchers.IO).launch {

        }

        args["path"] ?.let { path->

            args["fileName"]?.let { ouputFile->

                Main.main(arrayOf(
                    "-o", "${staticContentInteractor.staticContentRoot}/$ouputFile", "-d", path
                ))

                return "![UML for $path](/res/$ouputFile)"

            } ?: run {
                return usage
            }

        } ?: run {
            return "Usage:  path=/path/to/code"
        }
    }


}