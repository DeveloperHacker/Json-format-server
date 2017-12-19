import com.google.gson.*
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicInteger

/**
 * Server for json validation and formatting
 *
 * @port getting from program environment by key "PORT"
 * If port hasn't defined then 8000 assign to port
 */
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
            val errorId = JsonArray()
            errorId.add(JsonPrimitive(requestId.toString()))
            val resource = JsonArray()
            resource.add(JsonPrimitive("json string"))
            responseObject.add("errorCode", errorCode)
            responseObject.add("errorMessage", errorMessage)
            responseObject.add("errorPlace", errorPlace)
            responseObject.add("request-id", errorId)
            responseObject.add("resource", resource)
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
 