package ca.gbc.eventservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user", url = "${user.service.rul}")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value="/api/v1/user")
    String isStaff(@RequestParam String userId);
}
