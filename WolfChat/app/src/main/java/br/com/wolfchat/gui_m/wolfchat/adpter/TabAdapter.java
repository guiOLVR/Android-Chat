package br.com.wolfchat.gui_m.wolfchat.adpter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.wolfchat.gui_m.wolfchat.fragment.ContactFragment;
import br.com.wolfchat.gui_m.wolfchat.fragment.ConversationFragment;

public class TabAdapter extends FragmentStatePagerAdapter{

    private String[] tabsTitle = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ConversationFragment();
                break;
            case 1:
                fragment = new ContactFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabsTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitle[position];
    }
}
