package co.edu.uniminuto.buscadorlibros.api;

import co.edu.uniminuto.buscadorlibros.model.RespuestaLibros;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicioGoogleBooks {
    @GET("volumes")
    Call<RespuestaLibros> buscarLibros(
            @Query("q") String consulta,
            @Query("maxResults") int maxResultados,
            @Query("key") String apiKey
    );
}
