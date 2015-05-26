package br.com.indirim.quickphoto;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewPhotoFragment extends Fragment {

    ImageView previewImage;
    int THUMBNAIL_SIZE;

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

        String imagePath = getArguments().getString("br.com.indirim.quickphoto.IMAGE_PATH");
        Uri uri = Uri.parse(imagePath);
        previewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        previewImage.setImageBitmap(getPreview(uri, 200, 200));
    }

    public static PreviewPhotoFragment newInstance(String photoPath) {
        PreviewPhotoFragment fragment = new PreviewPhotoFragment();

        Bundle args = new Bundle();
        args.putString("br.com.indirim.quickphoto.IMAGE_PATH", photoPath);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    Bitmap getPreview(Uri uri, int width, int height) {
        Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(uri.getPath()), width, height);
        return resized;
    }
}
