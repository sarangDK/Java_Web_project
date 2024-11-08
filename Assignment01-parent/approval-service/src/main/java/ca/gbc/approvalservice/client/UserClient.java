package ca.gbc.approvalservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user", url = "${user.service.url}")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value="/api/v1/user")
    String isStaff(@RequestParam Long userId);
}
