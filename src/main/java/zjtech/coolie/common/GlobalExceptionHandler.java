package zjtech.coolie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public Mono<String> handleException(Exception e) {
    log.warn("An unexpected exception", e);
    return Mono.just("An unexpected exception");
  }
}
