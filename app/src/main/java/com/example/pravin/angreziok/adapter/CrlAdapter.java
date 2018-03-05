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

    public Crl getCrlByID(@Nullable String crlId) {
        if (crlId != null) {
            for (int i = 0; i < crls.size(); i++) {
                if (crlId == crls.get(i).getCRLId()) {
                    return crls.get(i);
                }
            }
        }
        return null;
    }
}
