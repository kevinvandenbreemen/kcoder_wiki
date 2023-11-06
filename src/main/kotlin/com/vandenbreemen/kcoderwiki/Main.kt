package com.vandenbreemen.kcoderwiki

import com.vandenbreemen.kcoderwiki.macro.UmlGeneratorMacro
import com.vandenbreemen.ktt.interactor.StaticContentInteractor
import com.vandenbreemen.ktt.main.WikiApplication

object Main {

    fun setup() {

        WikiApplication.macroRegistry.register(UmlGeneratorMacro(StaticContentInteractor()))

    }

    @JvmStatic
    fun main(args: Array<String>) {
        setup()
        WikiApplication.startup()
    }

}