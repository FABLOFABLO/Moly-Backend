package moly.backend.domain.memo.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.memo.domain.Memo;
import moly.backend.domain.memo.domain.repository.MemoRepository;
import moly.backend.domain.memo.presentation.dto.request.MemoCreateRequest;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoCreateService {
    private static final int WGS84_SRID = 4326;
    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), WGS84_SRID);

    private final MemoRepository memoRepository;
    private final UserRepository userRepository;

    @Transactional
    public void execute(String nickname, MemoCreateRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);

        Point location = GEOMETRY_FACTORY.createPoint(
                new Coordinate(request.longitude(), request.latitude())
        );
        location.setSRID(WGS84_SRID);

        Memo memo = Memo.builder()
                .content(request.content())
                .location(location)
                .user(user)
                .build();

        memoRepository.save(memo);
        userRepository.updateMemoCount(user.getId(), 1);
    }
}
