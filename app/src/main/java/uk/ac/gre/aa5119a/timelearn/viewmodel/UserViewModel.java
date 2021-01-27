package uk.ac.gre.aa5119a.timelearn.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.ac.gre.aa5119a.timelearn.model.User;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();

    public void setUser(User loggedInUser){
        user.setValue(loggedInUser);
    }

    public LiveData<User> getUser(){
        return user;
    }
}
