package uk.ac.gre.aa5119a.timelearn.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.ac.gre.aa5119a.timelearn.model.User;

public class AcademyViewModel extends ViewModel {

    private MutableLiveData<Integer> educationType = new MutableLiveData<>();

    public void setEducationType(int educationType){
        this.educationType.setValue(educationType);
    }

    public LiveData<Integer> getEducationType() {
        return educationType;
    }
}
