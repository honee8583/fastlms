package com.example.fastlms.course.service.impl;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.dto.TakeCourseDto;
import com.example.fastlms.course.entity.TakeCourse;
import com.example.fastlms.course.mapper.TakeCourseMapper;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.course.model.TakeCourseParam;
import com.example.fastlms.course.repository.TakeCourseRepository;
import com.example.fastlms.course.service.TakeCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TakeCourseServiceImpl implements TakeCourseService {

    private final TakeCourseRepository takeCourseRepository;
    private final TakeCourseMapper takeCourseMapper;

    @Override
    public List<TakeCourseDto> list(TakeCourseParam takeCourseParam) {

        long totalCount = takeCourseMapper.selectListCount(takeCourseParam);
        List<TakeCourseDto> list = takeCourseMapper.selectList(takeCourseParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (TakeCourseDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - takeCourseParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public TakeCourseDto detail(long id) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (optionalTakeCourse.isPresent()) {
            return TakeCourseDto.of(optionalTakeCourse.get());
        }

        return null;
    }

    @Override
    public ServiceResult updateStatus(long id, String status) {

        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (!optionalTakeCourse.isPresent()) {
            return new ServiceResult(false, "수강정보가 존재하지 않습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();

        takeCourse.setStatus(status);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true);
    }

    @Override
    public List<TakeCourseDto> myCourse(String userId) {

        TakeCourseParam takeCourseParam = new TakeCourseParam();
        takeCourseParam.setUserId(userId);
        List<TakeCourseDto> list =
                takeCourseMapper.selectListMyCourse(takeCourseParam);

        return list;
    }

    @Override
    public ServiceResult cancel(long id) {

        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (!optionalTakeCourse.isPresent()) {
            return new ServiceResult(false, "수강정보가 존재하지 않습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();

        takeCourse.setStatus(TakeCourse.STATUS_CANCEL);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true);
    }
}
