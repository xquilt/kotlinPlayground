import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Determine the base API URL of kotlinlang
 *
 * @return Base url to be used with API requests
 */
private fun generateBaseUrl(): String {
    // todo: Maybe another separate kotlin file, that would be used to scrape the versions from time to time cache the list.
    val versions = listOf(
        "1.2.71",
        "1.3.72",
        "1.4.30",
        "1.5.31",
        "1.6.21",
        "1.7.21",
        "1.8.10",
        "1.8.21",
        "1.9.0"
    )
    val baseUrl: String = "https:/api.kotlinlang.org/api/${versions.last()}/"
    return (baseUrl)
}

/**
 * Read the file content being the source of the code
 *
 * @param fileName Absolute path of the file on the desk
 * @return Textual string of the file content
 */
private fun readCode(fileName: String): String {
    // todo: Check if a file is already existent or not, then read its content. Return a string of the file content Check if the return string is empty at higher-order functions, and if so, don't send a request.
    lateinit var sourceCode: String;
    return (sourceCode)
}

fun getSourceCode(sourceCodeFile: String): String {
    var tempSourceCode = """
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
    """.trimIndent()
    return (tempSourceCode)
}

private fun parseSourceCode(sourceCode: String): String {
    // Read the file. Check if the file is empty. Check if the return string is empty at higher-order functions, and if so, then don't send a request. Return the file content
    val gson = Gson()
    val jsonString: String = gson.toJson(
        mapOf(
            "args" to "",
            "confType" to "java",
            "files" to listOf(
                mapOf(
                    "name" to "FileKt",
                    "publicId" to "",
                    "text" to "${sourceCode}"
                )
            )
        )
    )
    return (jsonString)
}

data class ResponseForm(
    val errors: Errors,
    val exception: Exception,
    val text: String
) {
    data class Errors(
        val Filekt: List<FileKt>
    ) {
        data class FileKt(
            val className: String,
            val interval: Interval,
            val message: String,
            val severity: String
        ) {
            data class Interval(
                val start: Start,
                val end: End
            ) {
                data class Start(
                    val ch: Int,
                    val line: Int
                )
                data class End(
                    val ch: Int,
                    val line: Int
                )
            }
        }
    }
    data class Exception(
        val cause: Any,
        val fullName: String,
        val localizedMessage: Any,
        val message: String,
        val stackTrace: List<StackTrace>
    ) {
        data class StackTrace(
            val className: String,
            val fileName: String,
            val lineNumber: Int,
            val methodName: String
        )
    }
}
// Creating a retrofit instance
private val retrofit = Retrofit.Builder()
    .baseUrl(generateBaseUrl())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    private var arguments: String = ""

private interface KotlinApiInterface {
    @Headers(
        "user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36",
        "content-type: application/json; charset=UTF-8"
    )
    @POST("compiler/run")
    fun sendData(@Body requestBody: RequestBody): Call<ResponseForm>;
}


fun sendPostRequest(
    sourceCode: String,
    arguments: String,
    successCallback: (ResponseForm?) -> Unit,
    failureCallback: (Throwable) -> Unit,
): Unit {
    // Creating an instance of our API interface
    val apiService = retrofit.create(KotlinApiInterface::class.java)

    val requestBody = RequestBody.create(
        MediaType.parse("application/json"),
        Gson().toJson(
            mapOf(
                "args" to arguments,
                "confType" to "java",
                "files" to listOf(
                    mapOf(
                        "name" to "File.kt",
                        "publicId" to "",
                        "text" to sourceCode
                    )
                )
            )
        )
    )

    // Making an API call and handling the response
    val call = apiService.sendData(requestBody)
    call.enqueue(object : retrofit2.Callback<ResponseForm> {
        override fun onResponse(
            call: Call<ResponseForm>,
            response: retrofit2.Response<ResponseForm>
        ) {
            if (response.code() == 200) {
                successCallback(response.body())
            }
        }
        override fun onFailure(
            call: Call<ResponseForm>,
            throwable: Throwable
        ) {
            failureCallback(throwable)
        }
    })
}

// a kotlin function to convert a string as a json object then access the json object values

//var codeOutput: String = jsonObject.get("text").asString.replace("<outStream>", "")
//    .replace(Regex("<(/)?outStream>"), "")
