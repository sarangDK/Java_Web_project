package ca.gbc.eventservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class userClientStub {
    public static void stubUserCall(String userId) {
        stubFor(get(urlEqualTo("/api/user?userId=" + userId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("Staff")));
    }
}
