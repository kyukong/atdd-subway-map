package wooteco.subway.section;

import wooteco.subway.exception.section.SectionDuplicationException;

public class Section {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Section() {
    }

    public Section(Long upStationId, Long downStationId, int distance) {
        this(null, upStationId, downStationId, distance);
    }

    public Section(Long id, Long upStationId, Long downStationId, int distance) {
        validateDuplicateUpAndDownIds(upStationId, downStationId);
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validateDuplicateUpAndDownIds(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new SectionDuplicationException();
        }
    }

    public boolean isUpStation(Long stationId) {
        return upStationId.equals(stationId);
    }

    public boolean isDownStation(Long stationId) {
        return downStationId.equals(stationId);
    }

    public boolean isSameOrLongDistance(Section newSection) {
        return this.distance <= newSection.distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}