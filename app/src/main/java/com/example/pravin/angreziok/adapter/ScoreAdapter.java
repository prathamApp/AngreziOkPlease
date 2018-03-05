package com.example.pravin.angreziok.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;


import com.example.pravin.angreziok.domain.Score;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {

    private final List<Score> scores;

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public ScoreAdapter(@NonNull Context context, @LayoutRes int resource, List<Score> scores) {
        super(context, resource, scores);
        this.scores = scores;
    }

    public Score getScoreById(@Nullable String studId) {
        if (studId != null) {
            for (int i = 0; i < scores.size(); i++) {
                if (studId == scores.get(i).getStudentID()) {
                    return scores.get(i);
                }
            }
        }
        return null;
    }
}
