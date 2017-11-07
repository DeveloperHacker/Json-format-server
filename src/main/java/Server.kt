import com.google.gson.*
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicInteger


fun main(args: Array<String>) {
    val maxRequestId = AtomicInteger(0)
    val parser = JsonParser()
    val builder = GsonBuilder().setPrettyPrinting().create()
    val server = HttpServer.create()
    val port = System.getenv("PORT")?.toInt() ?: 8000
    println("Available on port $port")
    server.bind(InetSocketAddress(port), 0)
    server.createContext("/") { exchange ->
        val requestId = maxRequestId.getAndIncrement()
        val responseObject = try {
            val string = exchange.requestBody.bufferedReader().readText()
            println("Request file: $string")
            parser.parse(string)
        } catch (ex: Exception) {
            val parts = ex.message!!.split(":").last().split(" at ")
            val message = parts.first().trim()
            val place = parts.drop(1).joinToString("").trim()
            val responseObject = JsonObject()
            val errorMessage = JsonArray()
            errorMessage.add(JsonPrimitive(message))
            val errorPlace = JsonArray()
            errorPlace.add(JsonPrimitive(place))
            val errorCode = JsonPrimitive(errorMessage.hashCode())
            val errorId = JsonPrimitive(requestId)
            responseObject.add("errorCode", errorCode)
            responseObject.add("errorMessage", errorMessage)
            responseObject.add("errorPlace", errorPlace)
            responseObject.add("request-id", errorId)
            responseObject
        }
        val response = builder.toJson(responseObject) + "\n"
        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.write(response.toByteArray())
        exchange.responseBody.close()
    }
    server.executor = null
    server.start()
}
 