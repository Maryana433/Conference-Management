package pl.maryana.conference.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.repository.LectureThematicPathRepository;
import pl.maryana.conference.service.LectureThematicPathService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("LectureService Tests")
@ExtendWith(MockitoExtension.class)
public class LectureThematicPathServiceTest {

    @Mock
    private LectureThematicPathRepository lectureThematicPathRepository;
    private LectureThematicPathService lectureThematicPathService;

    @BeforeEach
    void init(){
        lectureThematicPathService = new LectureThematicPathServiceImpl(lectureThematicPathRepository);
    }

    @Test
    void shouldFindAllLectures(){

        lectureThematicPathService.findAll();

        verify(lectureThematicPathRepository).findAll();
        verifyNoMoreInteractions(lectureThematicPathRepository);
    }

    @Test
    void shouldFindLectureById(){
        //given
        long lectureId = 1L;
        Lecture lecture = new Lecture();
        lecture.setId(lectureId);
        when(lectureThematicPathRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

        //when
        Lecture lectureFromService = lectureThematicPathService.findById(lectureId).get();

        //then
        assertEquals(lecture, lectureFromService);
    }

    @Test
    void shouldFindAllThematicPaths(){
        lectureThematicPathService.findAllThematicPaths();

        verify(lectureThematicPathRepository).findAllThematicPaths();
        verifyNoMoreInteractions(lectureThematicPathRepository);
    }


}
