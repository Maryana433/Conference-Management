package pl.maryana.conference.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.maryana.conference.exception.LectureNotFound;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.repository.LectureRepository;
import pl.maryana.conference.service.LectureService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("LectureService Tests")
@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;
    private LectureService lectureService;

    @BeforeEach
    void init(){
        lectureService = new LectureServiceImpl(lectureRepository);
    }

    @Test
    void shouldFindAllLectures(){

        lectureService.findAll();

        verify(lectureRepository).findAll();
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    void shouldFindLectureById(){
        //given
        long lectureId = 1L;
        Lecture lecture = new Lecture();
        lecture.setId(lectureId);
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        //when
        Lecture lectureFromService = lectureService.findById(lectureId).get();

        //then
        assertEquals(lecture, lectureFromService);
    }


}
