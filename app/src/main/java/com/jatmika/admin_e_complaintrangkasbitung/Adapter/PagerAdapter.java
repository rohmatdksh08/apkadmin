package com.jatmika.admin_e_complaintrangkasbitung.Adapter;

import com.jatmika.admin_e_complaintrangkasbitung.FragmentIUMK;
import com.jatmika.admin_e_complaintrangkasbitung.FragmentInfrastruktur;
import com.jatmika.admin_e_complaintrangkasbitung.FragmentKTP;
import com.jatmika.admin_e_complaintrangkasbitung.FragmentKependudukan;
import com.jatmika.admin_e_complaintrangkasbitung.FragmentNikah;
import com.jatmika.admin_e_complaintrangkasbitung.FragmentSPPT;
import com.jatmika.admin_e_complaintrangkasbitung.FragmentTutupJalan;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int number_tabs;

    public PagerAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentKTP();
            case 1:
                return new FragmentIUMK();
            case 2:
                return new FragmentKependudukan();
            case 3:
                return new FragmentNikah();
            case 4:
                return new FragmentSPPT();
            case 5:
                return new FragmentTutupJalan();
                default :
                    return null;
        }
    }

    @Override
    public int getCount() {
        return number_tabs;
    }
}
