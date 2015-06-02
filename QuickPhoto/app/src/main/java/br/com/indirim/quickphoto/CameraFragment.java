package br.com.indirim.quickphoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Objeto que representa a câmera
     */
    private Camera camera;

    /**
     * Local de armazenamento da foto tirada
     */
    private File imageFile;

    /**
     * View para exibição do preview da imagem
     */
    private SurfaceView surfaceView;

    private ImageButton effectsButton;

    private ImageButton negativeButton;
    private ImageButton monoButton;
    private ImageButton laplacianButton;
    private ImageButton sephiaButton;
    private ImageButton borderButton;
    private ImageButton normalButton;

    private File picsDir;

    private Camera.Parameters cParameters;

    private Camera.Parameters getCameraParameters()
    {
        if (cParameters == null)
            cParameters = camera.getParameters();

        return cParameters;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    Fragment effectsFragment;
    LinearLayout effects_container;

    private void toggleButtonVisibility(ImageButton b)
    {
        if (b.getVisibility() == View.INVISIBLE)
        {
            b.setVisibility(View.VISIBLE);
        } else {
            b.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleEffectButton()
    {
        toggleButtonVisibility(negativeButton);
        toggleButtonVisibility(monoButton);
        toggleButtonVisibility(laplacianButton);
        toggleButtonVisibility(sephiaButton);
        toggleButtonVisibility(borderButton);
        toggleButtonVisibility(normalButton);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        surfaceView = (SurfaceView) view.findViewById(R.id.cameraPreview);
        surfaceView.getHolder().addCallback(this);

        effectsButton = (ImageButton) view.findViewById(R.id.effectsButton);
        negativeButton = (ImageButton) view.findViewById(R.id.negativeButton);
        monoButton = (ImageButton) view.findViewById(R.id.monoButton);
        laplacianButton = (ImageButton) view.findViewById(R.id.laplacianButton);
        sephiaButton = (ImageButton) view.findViewById(R.id.sephiaButton);
        borderButton = (ImageButton) view.findViewById(R.id.borderButton);
        normalButton = (ImageButton) view.findViewById(R.id.normalButton);

        effectsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toggleEffectButton();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getCameraParameters().setColorEffect(Camera.Parameters.EFFECT_NEGATIVE);
                createCameraPreview(surfaceView.getHolder());
            }
        });
        monoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getCameraParameters().setColorEffect(Camera.Parameters.EFFECT_MONO);
                createCameraPreview(surfaceView.getHolder());
            }
        });
        laplacianButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Activity activity = getActivity();
                Toast.makeText(activity, "Laplacian filter", Toast.LENGTH_LONG).show();
            }
        });
        sephiaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getCameraParameters().setColorEffect(Camera.Parameters.EFFECT_SEPIA);
                createCameraPreview(surfaceView.getHolder());
            }
        });
        borderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Activity activity = getActivity();
                Toast.makeText(activity, "Border filter", Toast.LENGTH_LONG).show();
            }
        });
        normalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getCameraParameters().setColorEffect(Camera.Parameters.EFFECT_NONE);
                createCameraPreview(surfaceView.getHolder());
            }
        });

        toggleEffectButton();

        view.findViewById(R.id.photoButton).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        picsDir = MainActivity.getPhotoDirectory();
        if (!picsDir.exists() && !picsDir.mkdirs()) {
            Log.e("CameraFragment", "Falha ao criar o diretório de foto da aplicação");
        }
        camera = Camera.open();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.photoButton:
                takePicture(v);
                break;
        }
    }

    /**
     * Método que tira uma foto
     * @param v
     */
    public void takePicture(View v) {
        // Cria uma classe que implementa a interface PictureCallback, que será usada como callback ao tirar uma foto
        Camera.PictureCallback jpeg = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos = null;
                try {
                    try {
                        // Grava os bytes da imagem no arquivo onde a foto deve ser armazenada
                        fos = new FileOutputStream(imageFile);
                        fos.write(data);

                        Activity activity = getActivity();

                        // Mostra uma mensagem indicando que a foto foi tirada
                        Toast.makeText(activity, "Foto gravada em " + imageFile.getPath(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.setClass(activity, MainActivity.class);
                        intent.putExtra(MainActivity.IMAGE_PATH, imageFile.getAbsolutePath());
                        startActivity(intent);
                    } finally {
                        if (fos != null) {
                            fos.close();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ajustaOrientacaoDaCamera();

                // Inicia novamente o preview
                camera.startPreview();
            }
        };

        DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
        Date dt = new Date();

        imageFile = new File(picsDir, "IMG_" + df.format(dt) + ".jpg");

        // Tira uma foto. O callback fornecido é chamado assim que a imagem JPEG estiver disponível
        camera.takePicture(null, null, jpeg);
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link android.view.Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            ajustaOrientacaoDaCamera();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        createCameraPreview(holder);
    }

    private void createCameraPreview(SurfaceHolder holder)
    {
        if (holder.getSurface() != null) {
            try {
                camera.stopPreview();
            } catch (Exception e) {
            }

            try {
                ajustaOrientacaoDaCamera();
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private void ajustaOrientacaoDaCamera() {
        if (cParameters == null)
            cParameters = camera.getParameters();

        Activity activity = getActivity();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        camera.setDisplayOrientation(ORIENTATIONS.get(rotation));
        cParameters.setRotation(ORIENTATIONS.get(rotation));

        List<Camera.Size> previewSizes = cParameters.getSupportedPreviewSizes();

        Camera.Size previewSize = getOptimalPreviewSize(
                previewSizes,
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels);

        cParameters.setPreviewSize(previewSize.width, previewSize.height);

        List<String> focus = cParameters.getSupportedFocusModes();
        cParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);

        camera.setParameters(cParameters);
    }

    private void releaseCamera()
    {
        if (camera!= null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
