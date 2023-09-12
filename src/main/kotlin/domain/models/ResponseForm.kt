package domain.models

data class ResponseForm(
    val errors: Errors,
    val exception: Exception,
    val text: String
)
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