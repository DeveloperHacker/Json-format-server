import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

class EchoHandler : HttpHandler {
    override fun handle(exchange: HttpExchange) {
        val response = try {
            val parser = JsonParser()
            val builder = GsonBuilder().setPrettyPrinting().create()
            val string = exchange.requestBody.bufferedReader().readText()
            val element = parser.parse(string)
            builder.toJson(element) + "\n"
        } catch (ex: Exception) {
            ex.message + "\n"
        }
        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.write(response.toByteArray())
        exchange.responseBody.close()
    }
}


fun main(args: Array<String>) {
    val server = HttpServer.create()
    server.bind(InetSocketAddress(80), 0)
    server.createContext("/", EchoHandler())
    server.executor = null
    server.start()
}
 