package br.com.indirim.quickphoto;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewPhotoFragment extends Fragment {

    ImageView previewImage;

    public PreviewPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewImage = (ImageView) view.findViewById(R.id.previewImage);

        String imagePath = getArguments().getString(MainActivity.IMAGE_PATH);
        int imageRotate = getArguments().getInt(MainActivity.IMAGE_ROTATE);

        Uri uri = Uri.parse(imagePath);
        setImageView(uri, imageRotate);
    }

    public static PreviewPhotoFragment newInstance(String photoPath, int rotate) {
        PreviewPhotoFragment fragment = new PreviewPhotoFragment();

        Bundle args = new Bundle();
        args.putString(MainActivity.IMAGE_PATH, photoPath);
        args.putInt(MainActivity.IMAGE_ROTATE, rotate);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void setImageView(Uri uri, int rotate)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getScaledWidth(100), bitmap.getScaledHeight(100), false);
        bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        previewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        previewImage.setImageBitmap(bitmap);
    }
}
