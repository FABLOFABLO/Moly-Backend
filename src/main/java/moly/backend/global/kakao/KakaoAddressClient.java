package moly.backend.global.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KakaoAddressClient {
    private static final String ADDRESS_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
    private final RestClient restClient = RestClient.create();

    @Value("${kakao.rest-api-key}")
    private String restApiKey;

    public KakaoAddress findAddress(double longitude, double latitude) {
        try {
            KakaoAddressResponse response = restClient.get()
                    .uri(URI.create(ADDRESS_URL + "?x=" + longitude + "&y=" + latitude
                            + "&input_coord=WGS84"))
                    .header("Authorization", "KakaoAK " + restApiKey)
                    .retrieve()
                    .body(KakaoAddressResponse.class);
            return KakaoAddress.from(response);
        } catch (RestClientException exception) {
            throw new KakaoAddressLookupFailedException();
        }
    }

    private record KakaoAddressResponse(List<KakaoAddressDocument> documents) {
    }

    private record KakaoAddressDocument(
            KakaoAddressInfo address,
            @JsonProperty("road_address") KakaoRoadAddressInfo roadAddress
    ) {
    }

    private record KakaoAddressInfo(@JsonProperty("address_name") String addressName) {
    }

    private record KakaoRoadAddressInfo(@JsonProperty("address_name") String addressName) {
    }

    public record KakaoAddress(String address, String roadAddress) {
        private static KakaoAddress from(KakaoAddressResponse response) {
            if (response == null || response.documents() == null || response.documents().isEmpty()) {
                throw new KakaoAddressLookupFailedException();
            }

            KakaoAddressDocument document = response.documents().getFirst();
            if (document.address() == null || document.address().addressName() == null) {
                throw new KakaoAddressLookupFailedException();
            }

            String roadAddress = document.roadAddress() == null
                    ? null : document.roadAddress().addressName();
            return new KakaoAddress(document.address().addressName(), roadAddress);
        }
    }

    private static class KakaoAddressLookupFailedException extends CustomException {
        private KakaoAddressLookupFailedException() {
            super(ErrorCode.KAKAO_ADDRESS_LOOKUP_FAILED);
        }
    }
}
