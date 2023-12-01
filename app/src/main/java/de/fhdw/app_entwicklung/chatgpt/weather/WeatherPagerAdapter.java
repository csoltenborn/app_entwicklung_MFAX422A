package de.fhdw.app_entwicklung.chatgpt.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WeatherPagerAdapter extends FragmentStateAdapter {
    private int daysCount;
    private int startDay;

    public WeatherPagerAdapter(@NonNull FragmentActivity fragmentActivity, int startDay, int daysCount) {
        super(fragmentActivity);
        this.daysCount = daysCount;
        this.startDay = startDay;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new DayFragment(startDay + position);
    }

    @Override
    public int getItemCount() {
        return daysCount;
    }
}