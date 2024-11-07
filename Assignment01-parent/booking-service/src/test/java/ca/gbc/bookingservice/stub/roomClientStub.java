package ca.gbc.bookingservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class roomClientStub {

    public static void stubRoomCall(Long roomId, boolean roomAvailability) {
        stubFor(get(urlEqualTo("/api/rooms?roomId=" + roomId + "&roomAvailability=" + roomAvailability))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("true")));
    }
}

