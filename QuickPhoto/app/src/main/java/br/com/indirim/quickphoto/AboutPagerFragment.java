package br.com.indirim.quickphoto;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Fragment representando cada página da About, nela é includa a Foto eo RA do integrante.
 */
public class AboutPagerFragment extends Fragment {
    private int page;
    ImageView aboutImageContainer;

    public static String CURRENT_PAGE = "current_page";
    private TextView numero_ra;

    /**
     * Método responsável por criar uma nova instância do fragmento
     * @param Número da página criada
     * @return Uma nova instância do AboutPagerFragment
     */
    public static AboutPagerFragment newInstance(int page) {
        AboutPagerFragment fragmentFirst = new AboutPagerFragment();
        Bundle args = new Bundle();
        args.putInt(AboutPagerFragment.CURRENT_PAGE, page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(AboutPagerFragment.CURRENT_PAGE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_pager, container, false);
        aboutImageContainer = (ImageView) view.findViewById(R.id.about_image_container);
        numero_ra = (TextView)  view.findViewById(R.id.numero_ra);

        setImageView();
        setRA();

        return view;
    }

    /**
     * Define qual o RA será exibido na tela
     */
    private void setRA()
    {
        ArrayList<String> ras = new ArrayList<String>();

        ras.add("RA 20400402");
        ras.add("RA 20419299");
        ras.add("RA 20374685");
        ras.add("RA 20250751");
        ras.add("RA 20411930");

        numero_ra.setText(ras.get(page));
    }

    /**
     * Define qual foto será exibida.
     */
    private void setImageView()
    {
        Resources res = this.getResources();

        int id = 0;

        switch (page)
        {
            case 0:
                id = R.drawable.paulo;
                break;
            case 1:
                id = R.drawable.giani;
                break;
            case 2:
                id = R.drawable.julio;
                break;
            case 3:
                id = R.drawable.ben;
                break;
            case 4:
                id = R.drawable.jorge;
                break;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(res, id);
        bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getScaledWidth(100), bitmap.getScaledHeight(100), false);
        aboutImageContainer.setImageBitmap(bitmap);
    }
}
