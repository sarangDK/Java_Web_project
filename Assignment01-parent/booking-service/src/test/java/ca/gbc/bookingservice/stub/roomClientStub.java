package ca.gbc.bookingservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class roomClientStub {

    public static void stubRoomCall(Long room_id, boolean room_availability) {
        stubFor(get(urlEqualTo("/api/rooms?room_id=" + room_id + "&room_availability=" + room_availability))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("true")));
    }
}
