package it.delbix.coffeecounter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform