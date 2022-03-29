 package com.tatvasoft.tatvasoftassignment12.Adapter;

 import androidx.annotation.NonNull;
 import androidx.fragment.app.Fragment;
 import androidx.fragment.app.FragmentActivity;
 import androidx.viewpager2.adapter.FragmentStateAdapter;

 import com.tatvasoft.tatvasoftassignment12.Fragment.AudioFragment;
 import com.tatvasoft.tatvasoftassignment12.Fragment.ContactFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private AudioFragment audioFragment;
    private ContactFragment contactFragment;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1){
            if(audioFragment == null){
                audioFragment = new AudioFragment();
            }
            return new AudioFragment();
        }else{
            if (contactFragment == null){
                contactFragment = new ContactFragment();
            }
            return new ContactFragment();
        }


    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
