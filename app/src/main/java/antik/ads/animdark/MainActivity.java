package antik.ads.animdark;


/*
 * Created by aantik
 * 3/29/2026 1:10 PM
 *
 *   ⋆    ႔ ႔
 *     ᠸ^ ^ ⸝⸝
 *       |、˜〵
 *      じしˍ,)⁐̤ᐷ
 *
 * Fox Mode 🍺
 */


import android.animation.*;
import android.app.Activity;
import android.graphics.Color;
import android.os.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {
    int[] antik = {Color.parseColor("#FAFAFA"), Color.parseColor("#383838")};
    private boolean mod;
    private FrameLayout root;
    private ImageView btn;
    private View overlayop;

    private TextView tex_T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root_view);
        btn = findViewById(R.id.theme);
        tex_T = findViewById(R.id.iamT);
        btn.setColorFilter(Color.BLACK);
        overlayop = new View(this);
        overlayop.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        overlayop.setVisibility(View.INVISIBLE);
        root.addView(overlayop);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JumAnimation rip = JumAnimation.create(v);
                rip.setDuration(450);
                rip.start();
                anim(v);
            }
        });
    }

    private void anim(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator fade = ObjectAnimator.ofFloat(overlayop, "alpha", 0.35f, 0f);

            fade.setDuration(500);
            fade.setStartDelay(150);
            fade.start();

            fade.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    /*
                     * overlayop  setVisibility for par rip animation
                     * @aantik_mods
                     */
                 overlayop.setVisibility(View.INVISIBLE);
                }
            });
        }
        /*
         Aantik Mods (Dark Mode)
         */

        if (mod == true) {
            mod = false;
            root.setBackgroundColor(antik[0]);
            btn.setImageResource(R.drawable.brightness_3_24px);
            btn.setColorFilter(antik[1]);
            tex_T.setTextColor(antik[1]);
        } else {
            mod = true;
            root.setBackgroundColor(antik[1]);
            btn.setImageResource(R.drawable.wb_sunny_24px);
            tex_T.setTextColor(antik[0]);
            btn.setColorFilter(antik[0]);
        }
    }
}