package tinkoffapiservice.exception;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ServiceException {
    String error;
}
