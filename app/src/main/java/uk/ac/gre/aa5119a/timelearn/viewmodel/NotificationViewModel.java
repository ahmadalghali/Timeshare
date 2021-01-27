package uk.ac.gre.aa5119a.timelearn.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewModel extends ViewModel {

    private MutableLiveData<List<Object>> allUserNotifications = new MutableLiveData<>();

    public void setAllUserNotifications(MutableLiveData<List<Object>> allUserNotifications) {
        this.allUserNotifications = allUserNotifications;
    }
    public LiveData<List<Object>> getAllUserNotifications() {
        return allUserNotifications;
    }


}
