package uk.ac.gre.aa5119a.timelearn.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;

public class LessonViewModel extends ViewModel {


    private MutableLiveData<Integer> lessonId = new MutableLiveData<>();

    public LiveData<Integer> getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId.setValue(lessonId);
    }



    private MutableLiveData<LessonDTO> lesson = new MutableLiveData<>();

    public LiveData<LessonDTO> getLesson() {
        return lesson;
    }

    public void setLesson(LessonDTO lesson) {
        this.lesson.setValue(lesson);
    }
}
