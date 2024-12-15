package ru.practicum.workshop.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.workshop.userservice.dto.ResponseWithUserId;

@FeignClient(name = "registration-service-client", url = "http://localhost:8084")
public interface RegistrationClient {
    @GetMapping("/registrations/internal/user-auth")
    ResponseWithUserId confirmUser(@RequestHeader(value = "X-Registration-Id") Long registrationId,
                                   @RequestHeader(value = "X-Registration-Password") String registrationPassword);
}
