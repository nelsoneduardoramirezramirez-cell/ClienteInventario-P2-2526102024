package sv.edu.utec.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;

public class ProveedorAPI {

    private static final String BASE_URL = "https://dummyjson.com";
    public List<Producto> obtenerProductos(int limite) throws IOException, InterruptedException{
        String url = BASE_URL + "/products?limit=" + limite + "&select=title,stock";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode()!=200)
        {
            throw new IOException ("Error al consultar con proveedor. Codigo de estado: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        RespuestaProductos respuesta =mapper.readValue(response.body(),RespuestaProductos.class);

        List<Producto> productos = new ArrayList<>();

        for (ProductoApi productoAPI:respuesta.getProducts())
        {
            productos.add(productoAPI.aProducto());
        }

        return productos;
    }

}
//https://dummyjson.com/products?limit=10&select=title,stock