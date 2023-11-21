package com.vandenbreemen.kcoderwiki

import com.vandenbreemen.kcoderwiki.macro.UmlGeneratorMacro
import com.vandenbreemen.ktt.interactor.StaticContentInteractor
import com.vandenbreemen.ktt.macro.Macro
import com.vandenbreemen.ktt.main.WikiApplication

object Main {

    /**
     * Pre-register a macro before calling startup
     */
    fun registerMacro(macro: Macro) {
        WikiApplication.macroRegistry.register(macro)
    }

    fun setup() {

        WikiApplication.macroRegistry.register(UmlGeneratorMacro(StaticContentInteractor()))

    }

    /**
     * Start the main wiki
     */
    fun startup() {
        WikiApplication.startup()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        setup()
        startup()
    }

}