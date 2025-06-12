package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.DTO.AuthDTO.ApiUsdResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CotizationUsdService {
    private final RestTemplate restTemplate = new RestTemplate();
    private Double cachedVenta = null;
    private final String URL = "https://dolarapi.com/v1/dolares/oficial";


    public double obtenerCotizacionVenta(){
        if(cachedVenta == null){
            actualizarCotizacion();
        }
        return cachedVenta;
    }

    @Scheduled(fixedRate = 10*60*1000) // actualiza la cotizacion del dolar cada 10 minutos(aumentar o disminuir si queremos)
    public void actualizarCotizacion(){
        try{
            ResponseEntity<ApiUsdResponse> response = restTemplate.getForEntity(URL, ApiUsdResponse.class);
               ApiUsdResponse apiUsdResponse = response.getBody();

               if(apiUsdResponse != null && apiUsdResponse.getVenta() > 0){
                   cachedVenta = apiUsdResponse.getVenta();


               }else {
                   System.err.println("Respuesta invalida al actualizar cotizacion dolar.");
               }

        }catch (Exception e){
            System.out.println("ERROR: error al actualizar el precio del dolar: "+e.getMessage());
        }
    }

}
