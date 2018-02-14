package com.example.lionelgarcia.galapptest2;

import android.content.Context;
import android.content.res.TypedArray;
import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lgr on 24/01/2018.
 */

public class RadarSatelliteTick extends RelativeLayout {

    TextView mLabel;
    ImageView mTickImage;
    View mView;
    Context mContext;

    public RadarSatelliteTick(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sattelite_radar_point, this, true);

        mView = this.findViewById(R.id.main_layout);
        mLabel = this.findViewById(R.id.label);
        mTickImage = this.findViewById(R.id.tick_image);

        Random rand = new Random();

//        Animation ViewAnimation = AnimationUtils.loadAnimation(mContext, R.anim.blink_slow);
//        ViewAnimation.setStartOffset((long) rand.nextInt(500));
//        mView.startAnimation(ViewAnimation);
    }

    public RadarSatelliteTick(Context context) {
        this(context, null);
    }

    public void setTick(Satellite satellite){

        String prefix = satellite.getConstellationName().substring(0, 2);
        int tick;
        int label_color;

        tick = R.drawable.galileo_point;
        label_color = R.color.galileo_color;

//        switch (satellite.getMoperator()) {
//            case 1:
//                tick = R.drawable.gps_point;
//                label_color = R.color.gps_color;
//                break;
//            case 2:
//                tick = R.drawable.glonass_point;
//                label_color = R.color.glonass_color;
//                break;
//            case 3:
//                tick = R.drawable.sbas_point;
//                label_color = R.color.sbs_color;
//                break;
//            case 4:
//                tick = R.drawable.qzss_point;
//                label_color = R.color.qzss_color;
//                break;
//            case 5:
//                tick = R.drawable.beidou_point;
//                label_color = R.color.beidou_color;
//                break;
//            case 6:
//                tick = R.drawable.galileo_point;
//                label_color = R.color.galileo_color;
//                break;
//            default:
//                tick = R.drawable.galileo_point;
//                label_color = R.color.galileo_color;
//                break;
//        }

        Log.e("operator", Integer.toString(satellite.getMoperator()));
        setLabel(prefix+satellite.getMid(), label_color);
        setTickImage(tick);


    }

    public void setLabel(String label, int color){

        mLabel.setText(label);
    }

    public void setTickImage(int tickImage){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), tickImage);
        mTickImage.setImageBitmap(bm);
    }

    public void setX(Float x){
        this.setX(x);
    }

    public void setY(Float y) {
        this.setX(y);
    }

}