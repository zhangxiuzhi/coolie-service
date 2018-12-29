package zjtech.coolie.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import zjtech.coolie.transfer.VideoTransferService;

@RestController
@RequestMapping("/transfer")
public class TransferController {

  @Autowired
  private VideoTransferService service;

  @GetMapping
  public Mono<String> submit() {
    service.transfer();
    return Mono.just("The jobs is submitted now");

  }
}
