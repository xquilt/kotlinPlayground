package data.network

import formatCodeOutput
import kotlinx.coroutines.runBlocking
import org.junit.Test
import parseOutput

import sendPostRequest

private enum class SourceCodes(val code: String) {
    VALID("fun main() {val kotlin = \"Hello world\"\n    println(kotlin)\n}"),
    LONG_VALID("""
            fun main() {
                println("Hello, world!")
                for (i in 1..10) {
                    println(i)
                }
                val names = listOf(1, 2, 3)
                for (i in 0..names.size) {
                    println(names[i])
                }
            }
        """.trimIndent()),
    SYNTAX_ERROR("fun main() {\n    print\n    val kotlin = \"🙂\n    println(kotlin)\n}"),
    EXCEPTION_ERROR("fun main() {\n    val kotlin = \"🙂\n    println(kotlin)\n}"),
    OUT_OF_MEMORY_EXCEPTION("fun main() { for (i in 1..1000000000L) { println(i) } }")
}

class BackendApiKtTest {

    @Test
    fun getSourceCodeTest() { }

    @Test
    fun testValidCode() {
        runBlocking {
            sendPostRequest(
                sourceCode = SourceCodes.OUT_OF_MEMORY_EXCEPTION.code,
                arguments = "",
                successCallback = {
                    it?.let {
                        if (it.isSuccessful) {
                            println(it.body())
                            println(it.body()?.exception?.parseOutput())
                        } else {
                            println("Erroneous!")
                        }
                    }
                }
            ) {
                it.printStackTrace()
            }
        }
    }

}