import com.google.gson.Gson
import domain.models.Exception
import domain.models.ResponseForm
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
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
    // FIXME: Sometimes when keeping on using a deprecated version (deprecated at the playground), it takes a considerable amount of time to load, which raised SocketTimeOutException
    val versions = listOf(
        "1.2.71",
        "1.3.72",
        "1.4.30",
        "1.5.31",
        "1.6.21",
        "1.7.21",
        "1.8.10",
        "1.8.21",
        "1.9.10"
    )
    return ("https:/api.kotlinlang.org/api/${versions.last()}/")
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

suspend fun sendPostRequest(
    sourceCode: String,
    arguments: String,
    successCallback: (Response<ResponseForm>?) -> Unit,
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
    val response = apiService.sendData(requestBody).awaitResponse()
    successCallback(response)

}

/**
 * A kotlin extension function of thr String class used to remove extraneous text from the code output.
 *
 */
fun String.formatCodeOutput() = this.replace(Regex("<(/)?outStream>"), "")
/**
 * A kotlin extension function of thr Exception data class used to parse exception messages.
 *
 */
fun Exception.parseOutput(): MutableList<String> {
    return mutableListOf<String>().apply {
        add("Exception in thread \"main\" ${this@parseOutput.fullName}: ${message}")
        this@parseOutput.stackTrace.forEach {
            this.add("at ${it.className}.${it.methodName} (${if (it.fileName == "null") it.fileName else ""}:${it.lineNumber})")
        }
    }
}
