package ca.gbc.bookingservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class roomClientStub {

    public static void stubRoomAvailabilityGet(Long roomId, boolean roomAvailability) {
        stubFor(get(urlEqualTo("/api/rooms/availability/" + roomId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(String.valueOf(roomAvailability))));
    }

    public static void stubRoomAvailabilityUpdate(Long roomId, boolean newAvailability) {
        stubFor(put(urlEqualTo("/api/rooms/availability/" + roomId + "?availability=" + newAvailability))
                .willReturn(aResponse()
                        .withStatus(200)));
    }
}
