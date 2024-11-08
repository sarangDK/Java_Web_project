package ca.gbc.eventservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", url = "${user.service.url}")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value="/api/v1/user/isStaff/{userId}")
    String isStaff(@PathVariable("userId") Long userId);
}
