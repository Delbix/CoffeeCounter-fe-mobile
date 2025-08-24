package it.delbix.coffeecounter.di

import it.delbix.coffeecounter.GlobalContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val composeModule = module {
    singleOf(::GlobalContext)
}