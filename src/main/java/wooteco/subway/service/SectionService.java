package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.web.dto.SectionRequest;
import wooteco.subway.web.exception.SubwayHttpException;

@Service
public class SectionService {

    private static final int ZERO = 0;
    private final SectionDao sectionDao;

    public SectionService(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> findSectionsByLineId(Long id) {
        return sectionDao.findSectionsByLineId(id);
    }

    public void validateOnlyOneStationExists(Long lineId, SectionRequest sectionRequest) {
        Long sectionCountOfSameUpStationId = sectionDao
                .countSectionByLineAndStationId(lineId, sectionRequest.getUpStationId());
        Long sectionCountOfSameDownStationId = sectionDao
                .countSectionByLineAndStationId(lineId, sectionRequest.getDownStationId());

        if (sectionCountOfSameUpStationId > ZERO && sectionCountOfSameDownStationId > ZERO) {
            throw new SubwayHttpException("추가하려는 구간의 상/하행역 둘다 노선에 존재");
        }
        if (sectionCountOfSameDownStationId == ZERO && sectionCountOfSameUpStationId == ZERO) {
            throw new SubwayHttpException("추가하려는 구간의 상/하행역 모두 노선에 없음");
        }
    }

    public void shortenPriorSectionIfExists(Long lineId, SectionRequest sectionRequest) {
        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getDownStationId();

        sectionDao.findPriorSection(lineId, upStationId, downStationId)
                .ifPresent(prior -> shortenPriorSection(lineId, prior, sectionRequest));
    }

    private void shortenPriorSection(Long lineId, Section priorSection,
            SectionRequest sectionRequest) {
        validateDistance(priorSection, sectionRequest);
        sectionDao.delete(priorSection.getId());
        addShortenedPriorSection(lineId, priorSection, sectionRequest);
    }

    private void validateDistance(Section priorSection, SectionRequest sectionRequest) {
        Integer priorDistance = priorSection.getDistance();
        Integer newDistance = sectionRequest.getDistance();
        if (priorDistance <= newDistance) {
            throw new SubwayHttpException("추가하려는 구간의 거리가 기존 구간거리보다 크거나 같습니다");
        }
    }

    private void addShortenedPriorSection(Long lineId, Section priorSection,
            SectionRequest sectionRequest) {
        Long priorUpId = priorSection.getUpStationId();
        Long priorDownId = priorSection.getDownStationId();
        Long requestUpId = sectionRequest.getUpStationId();
        Long requestDownId = sectionRequest.getDownStationId();
        Integer newDistance = priorSection.getDistance() - sectionRequest.getDistance();

        Long newUpId = null;
        Long newDownId = null;

        if (priorUpId.equals(requestUpId)) {
            newUpId = requestDownId;
            newDownId = priorDownId;
        } else if (priorDownId.equals(requestDownId)) {
            newUpId = priorUpId;
            newDownId = requestUpId;
        }
        Section shortenedPriorSection = new Section(lineId, newUpId, newDownId, newDistance);
        sectionDao.save(shortenedPriorSection);
    }

    public void validateLineHasMoreThanOneSection(Long lineId) {
        Long sectionCount = sectionDao.countSectionByLineId(lineId);
        if (sectionCount <= 1) {
            throw new SubwayHttpException("노선에 구간이 하나밖에 없습니다");
        }
    }

    public void mergePriorSectionsIfExists(Long lineId, Long stationId) {
        List<Section> sections = sectionDao.findSectionsByStationId(lineId, stationId);
        validateLineHasStation(sections);
        if (priorSectionExists(sections)) {
            mergePriorSections(lineId, stationId, sections);
        }
    }

    private void validateLineHasStation(List<Section> sections) {
        if (sections.isEmpty()) {
            throw new SubwayHttpException("역이 노선에 포함되지 않은 역임");
        }
    }

    private boolean priorSectionExists(List<Section> sections) {
        return sections.size() == 2;
    }

    private void mergePriorSections(Long lineId, Long stationId, List<Section> sections) {
        Section first = sections.get(ZERO);
        Section second = sections.get(1);

        Section mergedSection = getMergedSection(lineId, stationId, first, second);
        sectionDao.save(mergedSection);
    }

    private Section getMergedSection(Long lineId, Long stationId, Section first, Section second) {
        Integer newDistance = first.getDistance() + second.getDistance();
        Long newUpStationId = null;
        Long newDownStationId = null;

        if (isCorrectSectionOrder(first, second, stationId)) {
            newUpStationId = second.getUpStationId();
            newDownStationId = first.getDownStationId();
        } else if (isCorrectSectionOrder(second, first, stationId)) {
            newUpStationId = first.getUpStationId();
            newDownStationId = second.getDownStationId();
        }

        return new Section(
                lineId,
                newUpStationId,
                newDownStationId,
                newDistance);
    }

    private boolean isCorrectSectionOrder(Section upper, Section lower, Long stationId) {
        return upper.getUpStationId().equals(stationId)
                && lower.getDownStationId().equals(stationId);
    }

    public void addSection(Long lineId, Section section) {
        Section newSection = new Section(lineId, section);
        sectionDao.save(newSection);
    }

    public void deleteSectionByStationId(Long lineId, Long stationId) {
        sectionDao.deleteSectionsByStationId(lineId, stationId);
    }
}