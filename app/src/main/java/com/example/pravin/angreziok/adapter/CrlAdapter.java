package com.example.pravin.angreziok.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.domain.Crl;

import java.util.List;

public class CrlAdapter extends ArrayAdapter<Crl> {

    private final List<Crl> crls;

    public CrlAdapter(@NonNull Context context, @LayoutRes int resource, List<Crl> crls) {
        super(context, resource, crls);
        this.crls = crls;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Nullable
    @Override
    public Crl getItem(int position) {
        if (position == 0) {
            Crl crl = new Crl();
            crl.setCrl_id("0");
            crl.setCrl_name(getContext().getString(R.string.create_new_crl));
            return crl;
        }
        return super.getItem(position - 1);
    }

    public int getCrlPosition(@Nullable String crlId) {
        if (crlId != null) {
            for (int i = 0; i < crls.size(); i++) {
                if (crlId == crls.get(i).getCrl_id()) {
                    return i + 1;
                }
            }
        }
        return -1;
    }
}
