package com.paycell.orderserviceclient.controller;

import com.paycell.orderserviceclient.client.ProductClient;
import com.paycell.orderserviceclient.request.SubmitOrderRequest;
import com.paycell.orderserviceclient.response.SubmitOrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final ProductClient _productClient;

    public OrderController(ProductClient productClient){
        _productClient = productClient;
    }


    // @Retry ile feign client kullanımında karşı taraftaki sisteme erişince  @RateLimiter devreye girer.
    // eğer 5 ten fazla 100s istek atarsak ratelimtter fallback çalışır. fakat ikisi birlikte kullanıldığında
    // eğer retry policy çalışırsa ilk hatada hatayı 3 kez denemeden ratelimitter fallback döndürür.
    // bu sırayı değiştiremiyoruz ciddi sıkıntı.

    // Circuit Braker wait-duration-in-open-state=20s ayarı ile bir hata durumunda 20s
    // boyunca istekte hata alınmasa dahi isteği hata ile sonlandır. Burada devre OPEN statede kalır
    // 20s geçtikten sonra yapılan istekte bir runtime exception oluşmaz ise CLOSE state olarak devre devam eder.
    // fakat resilience4j.circuitbreaker.instances.ProductClient.minimum-number-of-calls=3 minimum 3 hatalı çağırda
    // 20s süreci başlat -> CLOSE to OPEN STATE  -> OPEN CLOSE döner önce HALF OPEN State döner. sonrası hata
    // almayana kadar istekler yürütülür. Hatalı ve hatasız çalışan senaryolara için genelde hesaplama işlemlerinde
    // runtime exception unchecked exception durumlarında daha çok gözlemleriz.EDGE CASE -> testlerinin
    // yetersiz kaldığı noktalarda çok karşılaşılan bir hizmet kesintisiz. Circuit Braker sistemdeki
    // geçici hatalara odaklanır.

    // api/v1/order/submit
    @PostMapping("submit")
    // @Retry(name = "ProductClient",fallbackMethod = "ProductClientRetryFallback")
    //@RateLimiter(name = "ProductClient", fallbackMethod = "ProductClientRatelimitterFallback")
    @CircuitBreaker(name = "ProductClient",fallbackMethod="ProductClientCircuitBrakerFallback")
    public ResponseEntity<SubmitOrderResponse> submitOrder(@RequestBody SubmitOrderRequest request){
        return _productClient.checkStock(request);
    }

    public ResponseEntity<String> ProductClientCircuitBrakerFallback(@RequestBody SubmitOrderRequest request, Throwable t){
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Uygulmada bir hata meydana geldi. Belirli bir süre sonra tekrar deneyiniz");
    }

    public ResponseEntity<SubmitOrderResponse> ProductClientRetryFallback(@RequestBody SubmitOrderRequest request, Throwable t){
        return  ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new SubmitOrderResponse(t.getMessage(),"Canceled"));
    }

    public ResponseEntity<String> ProductClientRatelimitterFallback(@RequestBody SubmitOrderRequest request, Throwable t){
        return  ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Request");
    }

}
