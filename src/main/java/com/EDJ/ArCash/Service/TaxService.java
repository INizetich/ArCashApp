package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.DTO.AuthDTO.TaxPesosResponse;
import com.EDJ.ArCash.DTO.AuthDTO.TaxUsdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxService {

    @Autowired
    private CotizationUsdService cotizationUsdService;

    public TaxPesosResponse calcularPesos(double monto){
        return calcularARS(monto, "ARS");
    }


    public TaxUsdResponse calcularUSD(double monto){
        double tipoCambio = cotizationUsdService.obtenerCotizacionVenta();
        double montoEnPesos = monto * tipoCambio;
        return calcularUSD(montoEnPesos, "USD");
    }



    private TaxPesosResponse calcularARS(double montoBase, String moneda){
         double iva = montoBase * 0.21;

         double total = montoBase + iva;

         TaxPesosResponse taxPesosResponse = new TaxPesosResponse();
         taxPesosResponse.setMontoOriginal(montoBase);
         taxPesosResponse.setMoneda(moneda);
         taxPesosResponse.setIVA(iva);
         taxPesosResponse.setTotalFinal(total);
         return taxPesosResponse;

    }

    private TaxUsdResponse calcularUSD(double montoBase, String moneda){
        double iva = montoBase * 0.21;
        double percepcionGanancias = montoBase * 0.30;


        double total = montoBase + iva + percepcionGanancias;

        TaxUsdResponse taxUsdResponse = new TaxUsdResponse();
        taxUsdResponse.setMontoOriginal(montoBase);
        taxUsdResponse.setMoneda(moneda);
        taxUsdResponse.setPrecioDolar(cotizationUsdService.obtenerCotizacionVenta());
        taxUsdResponse.setPercepcionGanancias(percepcionGanancias);
        taxUsdResponse.setIVA(iva);
        taxUsdResponse.setTotalFinal(total);

        return taxUsdResponse;
    }
}
