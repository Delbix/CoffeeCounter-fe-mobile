package it.insiel.coffeecounter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform