package br.com.indirim.quickphoto;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialPagerFragment extends Fragment {
    private int page;
    ImageView tutorialImageContainer;

    public static String CURRENT_PAGE = "current_page";

    public static TutorialPagerFragment newInstance(int page) {
        TutorialPagerFragment fragmentFirst = new TutorialPagerFragment();
        Bundle args = new Bundle();
        args.putInt(TutorialPagerFragment.CURRENT_PAGE, page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(TutorialPagerFragment.CURRENT_PAGE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_pager, container, false);
        tutorialImageContainer = (ImageView) view.findViewById(R.id.tutorial_image_container);

        setImageView();

        return view;
    }

    private void setImageView()
    {
        Resources res = this.getResources();

        int id = 0;

        switch (page)
        {
            case 0:
                id = R.drawable.screen0;
                break;
            case 1:
                id = R.drawable.screen1;
                break;
            case 2:
                id = R.drawable.screen2;
                break;
            case 3:
                id = R.drawable.screen3;
                break;
            case 4:
                id = R.drawable.screen4;
                break;
            case 5:
                id = R.drawable.screen5;
                break;
            case 6:
                id = R.drawable.screen6;
                break;
            case 7:
                id = R.drawable.screen7;
                break;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(res, id);
        bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getScaledWidth(100), bitmap.getScaledHeight(100), false);
        tutorialImageContainer.setImageBitmap(bitmap);
    }
}
